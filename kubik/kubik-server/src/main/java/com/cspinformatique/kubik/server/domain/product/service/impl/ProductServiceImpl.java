package com.cspinformatique.kubik.server.domain.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.domain.dilicom.service.DilicomImageService;
import com.cspinformatique.kubik.server.domain.dilicom.service.ReferenceNotificationService;
import com.cspinformatique.kubik.server.domain.kos.service.KosNotificationService;
import com.cspinformatique.kubik.server.domain.product.repository.ProductRepository;
import com.cspinformatique.kubik.server.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.product.service.SupplierService;
import com.cspinformatique.kubik.server.domain.purchase.service.PurchaseOrderDetailService;
import com.cspinformatique.kubik.server.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.server.domain.purchase.service.PurchaseSessionDetailService;
import com.cspinformatique.kubik.server.domain.purchase.service.ReceptionDetailService;
import com.cspinformatique.kubik.server.domain.purchase.service.RestockService;
import com.cspinformatique.kubik.server.domain.purchase.service.RmaDetailService;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerCreditDetailService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceDetailService;
import com.cspinformatique.kubik.server.domain.warehouse.service.InventoryCountService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingDiffService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingProductService;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Action;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Type;
import com.cspinformatique.kubik.server.model.product.AvailabilityCode;
import com.cspinformatique.kubik.server.model.product.BarcodeType;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.PriceType;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductDoubles;
import com.cspinformatique.kubik.server.model.product.ProductType;
import com.cspinformatique.kubik.server.model.product.ReturnType;
import com.cspinformatique.kubik.server.model.product.Supplier;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder.Status;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSessionDetail;
import com.cspinformatique.kubik.server.model.purchase.ReceptionDetail;
import com.cspinformatique.kubik.server.model.purchase.Restock;
import com.cspinformatique.kubik.server.model.purchase.RmaDetail;
import com.cspinformatique.kubik.server.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.warehouse.InventoryCount;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Resource
	KosNotificationService kosNotificationService;

	@Resource
	CustomerCreditDetailService customerCreditDetailService;

	@Resource
	DilicomImageService dilicomImageService;

	@Resource
	InventoryCountService inventoryCountService;

	@Resource
	InvoiceDetailService invoiceDetailService;

	@Resource
	ProductImageService productImageService;

	@Resource
	ProductInventoryService productInventoryService;

	@Resource
	ProductRepository productRepository;

	@Resource
	PurchaseOrderDetailService purchaseOrderDetailService;

	@Resource
	PurchaseOrderService purchaseOrderService;

	@Resource
	PurchaseSessionDetailService purchaseSessionDetailService;

	@Resource
	ReceptionDetailService receptionDetailService;

	@Resource
	ReferenceNotificationService referenceNotificationService;

	@Resource
	RestockService restockService;

	@Resource
	RmaDetailService rmaDetailService;

	@Resource
	StocktakingDiffService stocktakingDiffService;

	@Resource
	StocktakingProductService stocktakingProductService;

	@Resource
	SupplierService supplierService;

	@Resource
	Environment env;

	@Value("${kubik.dilicom.enabled}")
	private boolean dilicomEnabled;

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
				reference.getPublishEndDate(), reference.getThickness(), reference.getWidth(), reference.getHeight(),
				reference.getWeight(), reference.getExtendedLabel(), reference.getPublisher(),
				reference.getCollection(), reference.getAuthor(), reference.getPublisherPresentation(),
				reference.getIsbn(), reference.getSupplierReference(), reference.getCollectionReference(),
				reference.getTheme(), reference.getPublisherIsnb(), reference.getReplacingAReference(),
				reference.getReplacedByAReference(), reference.getReplacesEan13(), reference.getReplacedByEan13(),
				reference.getOrderableByUnit(),
				reference.getBarcodeType() != null ? BarcodeType.parseByCode(reference.getBarcodeType()) : null,
				reference.getMainReference(), reference.getSecondaryReference(), reference.getReferencesCount(), 0f,
				null, true, null, null, false);
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
	public int countByCategory(Category category) {
		return productRepository.countByCategory(category);
	}

	@Override
	public int countByImagesValidated(boolean imagesValidated) {
		return productRepository.countByImagesValidated(imagesValidated);
	}

	@Override
	public List<Integer> findAllIds() {
		return productRepository.findAllIds();
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		Page<Product> productPage = productRepository.findAll(pageable);

		if (dilicomEnabled) {
			for (Product product : productPage.getContent()) {
				calculateImageEncryptedKey(product);
			}
		}

		return productPage;
	}

	@Override
	public List<ProductDoubles> findAllProductDoubles() {
		return productRepository.findAllProductDoubles().stream()
				.map(ean13 -> new ProductDoubles(ean13, productRepository.findByEan13Doubles(ean13)))
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public Product findByEan13(String ean13) {
		if (ean13.length() == 10)
			return productRepository.findByEan13EndsWith(ean13);
		else
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

	public List<Product> findByEan13Doubles(String ean13) {
		return productRepository.findByEan13Doubles(ean13);
	}

	@Override
	public Iterable<Product> findBySupplier(Supplier supplier) {
		return productRepository.findBySupplier(supplier);
	}

	@Override
	public Product findOne(int id) {
		Product product = productRepository.findOne(id);

		if (dilicomEnabled)
			calculateImageEncryptedKey(product);

		return product;
	}

	@Override
	public Product findRandomByCategory(Category category) {
		Page<Product> result;
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

	@Override
	public Product findRandomByImagesValidated(boolean imagesValidated) {
		Page<Product> result = productRepository.findRandomByImagesValidated(imagesValidated, new PageRequest(0, 1));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@PostConstruct
	private void init() {
		for (Product product : productRepository.findInvalidEan13()) {

			save(product);
		}
	}

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

		for (InventoryCount inventoryCount : inventoryCountService.findByProduct(sourceProduct)) {
			inventoryCount.setProduct(targetProduct);

			inventoryCountService.save(inventoryCount);
		}

		stocktakingDiffService.findByProduct(sourceProduct).forEach(stocktakingDiffService::delete);

		stocktakingProductService.findByProduct(sourceProduct).forEach(stocktakingProductService::delete);

		referenceNotificationService.findByProduct(sourceProduct).ifPresent(referenceNotificationService::delete);

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
	public Product save(Product product, boolean skipKosNotification) {
		Assert.notNull(product, "Product is undefined");
		Assert.notNull(product.getEan13(), "Ean13 is undefined");
		Assert.notNull(product.getSupplier(), "Supplier is undefined");

		boolean updatePurchaseOrders = false;

		Product oldVersion = findByEan13(product.getEan13());
		
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

		if (!skipKosNotification && product.getWeight() != null) {
			// Create a kos notification for the product.
			kosNotificationService.createNewNotification(product.getId(), Type.PRODUCT, Action.UPDATE);
		}

		return product;
	}

	@Override
	public Page<Product> search(String query, Pageable pageable) {
		Page<Product> page = productRepository.search("%" + query + "%", pageable);

		if (dilicomEnabled) {
			for (Product product : page.getContent()) {
				calculateImageEncryptedKey(product);
			}
		}

		return page;
	}
}
