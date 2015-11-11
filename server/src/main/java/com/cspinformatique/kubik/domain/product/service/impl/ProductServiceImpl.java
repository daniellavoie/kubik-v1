package com.cspinformatique.kubik.domain.product.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cspinformatique.kubik.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.domain.dilicom.service.DilicomImageService;
import com.cspinformatique.kubik.domain.kos.service.KosNotificationService;
import com.cspinformatique.kubik.domain.product.repository.ProductRepository;
import com.cspinformatique.kubik.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.product.service.SupplierService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderDetailService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseSessionDetailService;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionDetailService;
import com.cspinformatique.kubik.domain.purchase.service.RestockService;
import com.cspinformatique.kubik.domain.purchase.service.RmaDetailService;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditDetailService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceDetailService;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.model.kos.KosNotification.Action;
import com.cspinformatique.kubik.model.kos.KosNotification.Type;
import com.cspinformatique.kubik.model.product.AvailabilityCode;
import com.cspinformatique.kubik.model.product.BarcodeType;
import com.cspinformatique.kubik.model.product.Category;
import com.cspinformatique.kubik.model.product.PriceType;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.ProductType;
import com.cspinformatique.kubik.model.product.ReturnType;
import com.cspinformatique.kubik.model.product.Supplier;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.model.purchase.PurchaseSessionDetail;
import com.cspinformatique.kubik.model.purchase.ReceptionDetail;
import com.cspinformatique.kubik.model.purchase.Restock;
import com.cspinformatique.kubik.model.purchase.RmaDetail;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;

@Service
public class ProductServiceImpl implements ProductService, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Resource
	private KosNotificationService kosNotificationService;

	@Resource
	private CustomerCreditDetailService customerCreditDetailService;

	@Resource
	private DilicomImageService dilicomImageService;

	@Resource
	private InvoiceDetailService invoiceDetailService;

	@Resource
	private ProductImageService productImageService;

	@Resource
	private ProductInventoryService productInventoryService;

	@Resource
	private ProductRepository productRepository;

	@Resource
	private PurchaseOrderDetailService purchaseOrderDetailService;

	@Resource
	private PurchaseOrderService purchaseOrderService;

	@Resource
	private PurchaseSessionDetailService purchaseSessionDetailService;

	@Resource
	private ReceptionDetailService receptionDetailService;

	@Resource
	private RestockService restockService;

	@Resource
	private RmaDetailService rmaDetailService;

	@Resource
	private SupplierService supplierService;

	@Resource
	private Environment env;

	private Set<String> productIdsCache;

	@Override
	public void afterPropertiesSet() throws Exception {
		LOGGER.info("Caching products.");

		productIdsCache = new HashSet<String>();

		Pageable pageRequest = new PageRequest(0, 200);
		Page<Product> page = null;
		do {
			page = findAll(pageRequest);

			for (Product product : page.getContent()) {
				productIdsCache.add(product.getEan13() + "-" + product.getSupplier().getEan13());
			}

			pageRequest = pageRequest.next();
		} while (page != null && page.getContent().size() != 0);

		LOGGER.info("Products product completed.");
	};

	@Override
	public Product buildProductFromReference(Reference reference) {
		Supplier supplier = supplierService.findByEan13(reference.getSupplierEan13());

		if (supplier == null) {
			supplier = supplierService.generateSupplierIfNotFound(reference.getSupplierEan13());
		}

		Integer productId = null;
		if (reference.isImportedInKubik()) {
			Product product = findByEan13AndSupplier(reference.getEan13(), supplier);

			if (product != null) {
				productId = product.getId();
			}
		}

		return new Product(productId, reference.getEan13(), supplier,
				AvailabilityCode.parseByCode(reference.getAvailability()),
				reference.getPriceType() != null ? PriceType.parseByCode(reference.getPriceType()) : null,
				reference.getPriceTaxIn(), reference.getSchoolbook(), reference.getTvaRate1(),
				reference.getPriceTaxOut1(), reference.getTvaRate2(), reference.getPriceTaxOut2(),
				reference.getTvaRate3(), reference.getPriceTaxOut3(), 0d,
				reference.getReturnType() != null ? ReturnType.parseByCode(reference.getReturnType()) : null,
				reference.getAvailableForOrder(), reference.getDatePublished(),
				reference.getProductType() != null ? ProductType.parseByCode(reference.getProductType()) : null,
				reference.getPublishEndDate(), reference.getStandardLabel(), reference.getCashRegisterLabel(),
				reference.getThickness(), reference.getWidth(), reference.getHeight(), reference.getWeight(),
				reference.getExtendedLabel(), reference.getPublisher(), reference.getCollection(),
				reference.getAuthor(), reference.getPublisherPresentation(), reference.getIsbn(),
				reference.getSupplierReference(), reference.getCollectionReference(), reference.getTheme(),
				reference.getPublisherIsnb(), reference.getReplacingAReference(), reference.getReplacedByAReference(),
				reference.getReplacesEan13(), reference.getReplacedByEan13(), reference.getOrderableByUnit(),
				reference.getBarcodeType() != null ? BarcodeType.parseByCode(reference.getBarcodeType()) : null,
				reference.getMainReference(), reference.getSecondaryReference(), reference.getReferencesCount(), 0f,
				null, true, null, null);
	}

	@Override
	public int countByCategory(Category category) {
		return productRepository.countByCategory(category);
	}

	@Override
	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		Page<Product> productPage = productRepository.findAll(pageable);

		for (Product product : productPage.getContent()) {
			calculateImageEncryptedKey(product);
		}

		return productPage;
	}

	@Override
	public Iterable<Product> findByEan13(String ean13) {
		return productRepository.findByEan13(ean13);
	}

	@Override
	public Product findByEan13AndSupplier(String ean13, Supplier supplier) {
		Assert.notNull(ean13);
		Assert.notNull(supplier);

		try {
			return productRepository.findByEan13AndSupplier(ean13, supplier);
		} catch (RuntimeException runtimeEx) {
			LOGGER.error("Error while querying ean13 " + ean13 + " for supplier " + supplier.getId());

			throw runtimeEx;
		}
	}

	@Override
	public Iterable<Product> findBySupplier(Supplier supplier) {
		return productRepository.findBySupplier(supplier);
	}

	@Override
	public Product findOne(int id) {
		Product product = productRepository.findOne(id);

		calculateImageEncryptedKey(product);

		return product;
	}

	@Override
	public Product findRandomByCategory(Category category) {
		Page<Product> result = null;
		Pageable pageable = new PageRequest(0, 1);

		if (category == null) {
			result = productRepository.findRandomWithoutCategory(pageable);
		} else {
			result = productRepository.findRandomByCategory(category, pageable);
		}

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	private void calculateImageEncryptedKey(Product product) {
		if (product.getSupplier() != null) {
			product.setImageEncryptedKey(
					dilicomImageService.getEncryptedUrl(product.getEan13(), product.getSupplier().getEan13()));
		}
	}

	private void calculateTaxesAmounts(Product product) {
		if (product.getTvaRate1() != null) {
			product.setPriceTaxOut1(Precision.round(product.getPriceTaxIn() / (1 + product.getTvaRate1() / 100), 2));
		}
		if (product.getTvaRate2() != null) {
			product.setPriceTaxOut1(Precision.round(product.getPriceTaxIn() / (1 + product.getTvaRate2() / 100), 2));
		}
		if (product.getTvaRate3() != null) {
			product.setPriceTaxOut1(Precision.round(product.getPriceTaxIn() / (1 + product.getTvaRate3() / 100), 2));
		}
	}

	@Override
	public Set<String> getProductIdsCache() {
		return productIdsCache;
	};

	@Override
	@Transactional
	public void mergeProduct(Product sourceProduct, Product targetProduct) {
		for (PurchaseSessionDetail purchaseSessionDetail : purchaseSessionDetailService.findByProduct(sourceProduct)) {
			purchaseSessionDetail.setProduct(targetProduct);

			purchaseSessionDetailService.save(purchaseSessionDetail);
		}

		for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetailService.findByProduct(sourceProduct)) {
			purchaseOrderDetail.setProduct(targetProduct);

			purchaseOrderDetailService.save(purchaseOrderDetail);
		}

		for (ReceptionDetail receptionDetail : receptionDetailService.findByProduct(sourceProduct)) {
			receptionDetail.setProduct(targetProduct);

			receptionDetailService.save(receptionDetail);
		}

		for (RmaDetail rmaDetail : rmaDetailService.findByProduct(sourceProduct)) {
			rmaDetail.setProduct(targetProduct);

			rmaDetailService.save(rmaDetail);
		}

		for (Restock restock : restockService.findByProduct(sourceProduct)) {
			restock.setProduct(targetProduct);

			restockService.save(restock);
		}

		for (CustomerCreditDetail customerCreditDetail : customerCreditDetailService.findByProduct(sourceProduct)) {
			customerCreditDetail.setProduct(targetProduct);

			customerCreditDetailService.save(customerCreditDetail);
		}

		for (InvoiceDetail invoiceDetail : invoiceDetailService.findByProduct(sourceProduct)) {
			invoiceDetail.setProduct(targetProduct);

			invoiceDetailService.save(invoiceDetail);
		}

		productInventoryService.deleteByProduct(sourceProduct);

		productInventoryService.updateInventory(targetProduct);

		productRepository.delete(sourceProduct);
	}

	@Override
	@Transactional
	public Product save(Product product) {
		return save(product, false);
	}

	@Override
	@Transactional
	public Product save(Product product, boolean skipBroadleafNotification) {
		boolean updatePurchaseOrders = false;
		Product oldVersion = null;

		if (product.getId() != null) {
			oldVersion = findOne(product.getId());
		}

		// Checks if the supplier has changed.
		if (oldVersion != null && !product.getSupplier().getEan13().equals(oldVersion.getSupplier().getEan13())) {
			// Makes sure no purchase orders exists for the product.
			if (purchaseOrderService.findByProduct(product).iterator().hasNext()) {
				throw new RuntimeException("Product can't change supplier as purchase orders exists for it.");
			}
		}

		// checks if the prices needs to be calculated.
		if (oldVersion == null || product.getPriceTaxIn() != oldVersion.getPriceTaxIn()) {
			calculateTaxesAmounts(product);

			updatePurchaseOrders = true;
		}

		// Saves the product.
		product = productRepository.save(product);

		if (updatePurchaseOrders) {
			purchaseOrderService.save(purchaseOrderService.findByProductAndStatus(product, Status.DRAFT));
		}

		if (!skipBroadleafNotification) {
			// Create a broadleaf notification for the product.
			kosNotificationService.createNewNotification(product.getId(), Type.PRODUCT, Action.UPDATE);
		}

		// Upload the product image to AWS S3.
		if (product.isDilicomReference()) {
			productImageService.persistProductImagesToAws(product.getId());
		}

		return product;
	}

	@Override
	public Page<Product> search(String query, Pageable pageable) {
		Page<Product> page = productRepository.search("%" + query + "%", pageable);

		for (Product product : page.getContent()) {
			calculateImageEncryptedKey(product);
		}

		return page;
	}
}
