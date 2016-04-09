package com.cspinformatique.kubik.server.domain.warehouse.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingProduct;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking.Status;

public interface StocktakingProductService {
	StocktakingProduct addProductToCategory(int productId, long categoryId);
	
	int countCategoriesWithProduct(int productId, long categoryId);
	
	void delete(long id);

	List<StocktakingProduct> findByProductAndStocktakingStatus(Product product, Status status);
	
	StocktakingProduct updateQuantity(long id, double quantity);
	
	StocktakingProduct save(StocktakingProduct stocktakingProduct);
}
