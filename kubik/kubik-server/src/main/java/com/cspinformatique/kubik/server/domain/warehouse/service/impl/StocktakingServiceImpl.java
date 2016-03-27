package com.cspinformatique.kubik.server.domain.warehouse.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.warehouse.repository.StocktakingRepository;
import com.cspinformatique.kubik.server.domain.warehouse.service.InventoryCountService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingCategoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingProductService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingService;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Product;
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
	private CategoryService categoryService;

	@Resource
	private InventoryCountService inventoryCountService;

	@Resource
	private ProductInventoryService productInventoryService;

	@Resource
	private ProductService productService;

	@Resource
	private StocktakingProductService stocktakingProductService;

	@Resource
	private StocktakingCategoryService stocktakingCategoryService;

	@Resource
	private StocktakingRepository stocktakingRepository;

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
	public void generateDummyStocktaking() {
		Stocktaking stocktaking = new Stocktaking();
		stocktaking.setStatus(Status.IN_PROGRESS);
		stocktaking.setCategories(new ArrayList<>());
		
		stocktaking = save(stocktaking);

		for (Category category : categoryService.findAll()) {
			StocktakingCategory stocktakingCategory = null;
			for (Product product : productService.findByCategory(category)) {
				if (product.getProductInventory() != null && product.getProductInventory().getQuantityOnHand()
						+ product.getProductInventory().getQuantityOnHold() != 0d) {
					if (stocktakingCategory == null) {
						stocktakingCategory = new StocktakingCategory();
						stocktakingCategory.setStocktaking(stocktaking);
						stocktakingCategory.setProducts(new ArrayList<>());
						stocktakingCategory.setName(category.getName());
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

		save(stocktaking);
	}

	@Override
	public void generateStocktakingDiffs(long id) {
		Stocktaking stocktaking = findOne(id);

		stocktaking.getDiffs().clear();

		Map<Integer, StocktakingProduct> stocktakingProducts = stocktaking.getCategories().stream()
				.flatMap(category -> category.getProducts().stream())
				.collect(Collectors.toMap(stocktakingProduct -> stocktakingProduct.getProduct().getId(),
						stocktakingProduct -> stocktakingProduct));

		for (int productId : productService.findAllIds()) {
			StocktakingProduct stocktakingProduct = stocktakingProducts.get(productId);

			Product product = null;
			double countedQuantity = 0;
			if (stocktakingProduct != null) {
				product = stocktakingProduct.getProduct();
				countedQuantity = stocktakingProduct.getQuantity();
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