package com.cspinformatique.kubik.server.domain.warehouse.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.warehouse.repository.StocktakingProductRepository;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingCategoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingDiffService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingProductService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.ProductInventory;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingCategory;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingProduct;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking.Status;

@Service
public class StocktakingProductServiceImpl implements StocktakingProductService {
	@Resource
	private StocktakingDiffService stocktakingDiffService;

	@Resource
	private StocktakingProductRepository stocktakingProductRepository;

	@Resource
	private ProductInventoryService productInventoryService;

	@Resource
	private ProductService productService;

	@Resource
	private StocktakingCategoryService stocktakingCategoryService;

	@Override
	public StocktakingProduct addProductToCategory(int productId, long categoryId) {
		Product product = productService.findOne(productId);
		StocktakingCategory category = stocktakingCategoryService.findOne(categoryId);

		StocktakingProduct stocktakingProduct;
		Optional<StocktakingProduct> optional = stocktakingProductRepository.findByProductAndCategory(product,
				category);

		if (optional.isPresent()) {
			stocktakingProduct = optional.get();
		} else {
			stocktakingProduct = new StocktakingProduct();
			stocktakingProduct.setCategory(category);
			stocktakingProduct.setProduct(product);
			stocktakingProduct.setQuantity(0);
		}

		stocktakingProduct.setQuantity(stocktakingProduct.getQuantity() + 1);

		return save(stocktakingProduct);
	}

	@Override
	public int countCategoriesWithProduct(int productId, long categoryId) {
		return stocktakingProductRepository.countCategoriesWithProduct(productId, categoryId);
	}

	@Override
	public void delete(long id) {
		stocktakingProductRepository.delete(id);
	}
	
	@Override
	public void delete(StocktakingProduct stocktakingProduct) {
		stocktakingProductRepository.delete(stocktakingProduct);
	}
	
	@Override
	public List<StocktakingProduct> findByProduct(Product product) {
		return stocktakingProductRepository.findByProduct(product);
	}

	@Override
	public List<StocktakingProduct> findByProductAndStocktakingStatus(Product product, Status status) {
		return stocktakingProductRepository.findByProductAndStocktakingStatus(product, status);
	}

	private void updateStocktakingDiffQuantity(Product product, double quantity) {
		// Check for active stocktaking concerning the product.
		Map<Stocktaking, List<StocktakingProduct>> stocktakingProductsByStocktaking = findByProductAndStocktakingStatus(
				product, Status.IN_PROGRESS).stream().collect(
						Collectors.groupingBy(stocktakingProduct -> stocktakingProduct.getCategory().getStocktaking()));

		for (List<StocktakingProduct> stocktakingProducts : stocktakingProductsByStocktaking.values()) {
			stocktakingProducts.stream().findFirst().ifPresent(stocktakingProduct -> {
				Optional<StocktakingDiff> optional = stocktakingProduct.getCategory().getStocktaking().getDiffs()
						.stream().filter(stocktakingDiff -> stocktakingDiff.getProduct().getId() == product.getId())
						.findAny();

				if (optional.isPresent()) {
					StocktakingDiff stocktakingDiff = optional.get();

					stocktakingDiffService.updateCountedQuantity(stocktakingDiff.getId(), quantity);
					stocktakingDiffService.updateValidated(stocktakingDiff.getId(), false);
				}
			});
		}
	}

	@Override
	public StocktakingProduct updateQuantity(long id, double quantity) {
		StocktakingProduct stocktakingProduct = stocktakingProductRepository.findOne(id);
		stocktakingProduct.setQuantity(quantity);

		updateStocktakingDiffQuantity(stocktakingProduct.getProduct(), quantity);

		return save(stocktakingProduct);
	}

	@Override
	public StocktakingProduct save(StocktakingProduct stocktakingProduct) {
		ProductInventory productInventory = productInventoryService.findByProduct(stocktakingProduct.getProduct());

		stocktakingProduct
				.setInventoryQuantity(productInventory.getQuantityOnHand() + productInventory.getQuantityOnHold());

		return stocktakingProductRepository.save(stocktakingProduct);
	}
}
