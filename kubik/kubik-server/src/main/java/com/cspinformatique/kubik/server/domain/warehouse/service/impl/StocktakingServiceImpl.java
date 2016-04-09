package com.cspinformatique.kubik.server.domain.warehouse.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.purchase.service.ReceptionService;
import com.cspinformatique.kubik.server.domain.purchase.service.RmaService;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.domain.warehouse.repository.StocktakingRepository;
import com.cspinformatique.kubik.server.domain.warehouse.service.InventoryCountService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingCategoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingDiffService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingProductService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingService;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.Reception;
import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus.Types;
import com.cspinformatique.kubik.server.model.warehouse.InventoryCount;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking.Status;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingCategory;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingProduct;

@Service
public class StocktakingServiceImpl implements StocktakingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StocktakingServiceImpl.class);

	@Resource
	CategoryService categoryService;

	@Resource
	CustomerCreditService customerCreditService;

	@Resource
	InventoryCountService inventoryCountService;

	@Resource
	InvoiceService invoiceService;

	@Resource
	ProductInventoryService productInventoryService;

	@Resource
	ProductService productService;

	@Resource
	ReceptionService receptionService;

	@Resource
	RmaService rmaService;

	@Resource
	StocktakingProductService stocktakingProductService;

	@Resource
	StocktakingCategoryService stocktakingCategoryService;

	@Resource
	StocktakingDiffService stocktakingDiffService;

	@Resource
	StocktakingRepository stocktakingRepository;

	public void applyInventoryAdjustments(long id) {
		Date since = findOne(id).getCreationDate();

		receptionService.findByStatusAndDateReceivedAfter(Reception.Status.CLOSED, since).stream()
				.flatMap(reception -> reception.getDetails().stream()).forEach(detail -> {
					applyInventoryAdjustments(detail.getProduct(), detail.getQuantityReceived());
				});

		invoiceService.findByStatusAndPaidDateAfter(new InvoiceStatus(Types.PAID.name(), null), since).stream()
				.flatMap(invoice -> invoice.getDetails().stream()).forEach(detail -> {
					applyInventoryAdjustments(detail.getProduct(), detail.getQuantity() * -1);
				});

		rmaService.findByStatusAndShippedDateAfter(Rma.Status.SHIPPED, since).stream()
				.flatMap(rma -> rma.getDetails().stream()).forEach(detail -> {
					applyInventoryAdjustments(detail.getProduct(), detail.getQuantity() * -1);
				});

		customerCreditService.findByStatusAndCompleteDateAfter(CustomerCredit.Status.COMPLETED, since).stream()
				.flatMap(customerCredit -> customerCredit.getDetails().stream()).forEach(detail -> {
					applyInventoryAdjustments(detail.getProduct(), detail.getQuantity());
				});

		inventoryCountService.findByDateCountedAfter(since).stream().forEach(inventoryCount -> {
			applyInventoryAdjustments(inventoryCount.getProduct(), inventoryCount.getQuantity());
		});
	}

	@Override
	public void applyInventoryAdjustments(Product product, double addedQuantity) {
		// Check for active stocktaking concerning the product.
		Map<Stocktaking, List<StocktakingProduct>> stocktakingProductsByStocktaking = stocktakingProductService
				.findByProductAndStocktakingStatus(product, Status.IN_PROGRESS).stream().collect(
						Collectors.groupingBy(stocktakingProduct -> stocktakingProduct.getCategory().getStocktaking()));

		for (List<StocktakingProduct> stocktakingProducts : stocktakingProductsByStocktaking.values()) {
			stocktakingProducts.stream().findFirst().ifPresent(stocktakingProduct -> {
				Optional<StocktakingDiff> optional = stocktakingProduct.getCategory().getStocktaking().getDiffs()
						.stream().filter(stocktakingDiff -> stocktakingDiff.getProduct().getId() == product.getId())
						.findAny();

				if (optional.isPresent()) {
					StocktakingDiff stocktakingDiff = optional.get();

					stocktakingDiffService.updateCountedQuantity(stocktakingDiff.getId(),
							stocktakingDiff.getCountedQuantity() + addedQuantity);
					stocktakingDiffService.updateValidated(stocktakingDiff.getId(), false);
				} else {
					stocktakingProductService.updateQuantity(stocktakingProduct.getId(),
							stocktakingProduct.getQuantity() + addedQuantity);
				}
			});
		}
	}

	private void computeInventory(Stocktaking stocktaking) {
		LOGGER.info("Computing inventory !");
	}

	@Override
	public List<Stocktaking> findAll() {
		return stocktakingRepository.findAll();
	}

	@Override
	public Stocktaking findOne(long id) {
		return stocktakingRepository.findOne(id);
	}

	@Override
	public Stocktaking generateDummyStocktaking() {
		Stocktaking stocktaking = new Stocktaking();
		stocktaking.setStatus(Status.IN_PROGRESS);
		stocktaking.setCategories(new ArrayList<>());

		stocktaking = save(stocktaking);

		List<Category> categories = categoryService.findAll();
		categories.add(null);
		
		for (Category category : categories) {
			StocktakingCategory stocktakingCategory = null;
			for (Product product : productService.findByCategory(category)) {
				if (product.getProductInventory() != null && product.getProductInventory().getQuantityOnHand()
						+ product.getProductInventory().getQuantityOnHold() != 0d) {
					if (stocktakingCategory == null) {
						stocktakingCategory = new StocktakingCategory();
						stocktakingCategory.setStocktaking(stocktaking);
						stocktakingCategory.setProducts(new ArrayList<>());
						stocktakingCategory.setName(category != null ? category.getName() : "Sans catégorie");
						stocktakingCategory = stocktakingCategoryService.save(stocktakingCategory);
						stocktaking.getCategories().add(stocktakingCategory);
					}

					StocktakingProduct stocktakingProduct = new StocktakingProduct();

					stocktakingCategory.getProducts().add(stocktakingProduct);

					stocktakingProduct.setProduct(product);
					stocktakingProduct.setCategory(stocktakingCategory);

					stocktakingProduct.setQuantity(product.getProductInventory().getQuantityOnHold()
							+ product.getProductInventory().getQuantityOnHand());

					stocktakingProductService.save(stocktakingProduct);
				}
			}
		}

		return save(stocktaking);
	}

	@Override
	public void generateStocktakingDiffs(long id) {
		Stocktaking stocktaking = findOne(id);

		stocktaking.getDiffs().clear();

		Map<Integer, List<StocktakingProduct>> stocktakingProductsMap = stocktaking.getCategories().stream()
				.flatMap(category -> category.getProducts().stream())
				.collect(Collectors.groupingBy(stocktakingProduct -> stocktakingProduct.getProduct().getId()));

		for (int productId : productService.findAllIds()) {
			List<StocktakingProduct> stocktakingProducts = stocktakingProductsMap.get(productId);

			Product product = null;
			double countedQuantity = 0;
			if (stocktakingProducts != null) {
				product = stocktakingProducts.stream().findFirst().get().getProduct();

				countedQuantity = stocktakingProducts.stream().mapToDouble(StocktakingProduct::getQuantity).sum();
			} else
				product = productService.findOne(productId);

			double inventoryQuantity = 0d;
			if (product.getProductInventory() != null) {
				inventoryQuantity = product.getProductInventory().getQuantityOnHand()
						+ product.getProductInventory().getQuantityOnHold();
			}

			if (countedQuantity != inventoryQuantity) {
				StocktakingDiff diff = new StocktakingDiff();

				diff.setProduct(product);
				diff.setCountedQuantity(countedQuantity);
				diff.setAdjustmentQuantity(countedQuantity - inventoryQuantity);
				diff.setStocktaking(stocktaking);

				stocktaking.getDiffs().add(diff);
			}
		}

		save(stocktaking);
	}

	@Override
	public Stocktaking save(Stocktaking stocktaking) {
		if (stocktaking.getCreationDate() == null)
			stocktaking.setCreationDate(new Date());

		if (stocktaking.getStatus() == null)
			stocktaking.setStatus(Status.IN_PROGRESS);

		if (Status.CANCELED.equals(stocktaking.getStatus()) && stocktaking.getCanceledDate() == null)
			stocktaking.setCanceledDate(new Date());
		else if (Status.COMPLETED.equals(stocktaking.getStatus()) && stocktaking.getCompletionDate() == null) {
			stocktaking.setCompletionDate(new Date());

			computeInventory(stocktaking);
		}

		return stocktakingRepository.save(stocktaking);
	}

	@Override
	public Stocktaking updateInventory(long stocktakingId) {
		Stocktaking stocktaking = findOne(stocktakingId);

		for (StocktakingDiff diff : stocktaking.getDiffs()) {
			if (diff.getAdjustmentQuantity() != 0) {
				InventoryCount inventoryCount = new InventoryCount();
				inventoryCount.setProduct(diff.getProduct());
				inventoryCount.setReason("Prise d'inventaire #" + stocktakingId);
				inventoryCount.setQuantity(diff.getAdjustmentQuantity());

				inventoryCountService.save(inventoryCount);
			}
		}

		stocktaking.setStatus(Status.COMPLETED);

		return save(stocktaking);
	}
}