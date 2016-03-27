package com.cspinformatique.kubik.server.domain.warehouse.service;

import com.cspinformatique.kubik.server.model.warehouse.StocktakingProduct;

public interface StocktakingProductService {
	StocktakingProduct addProductToCategory(int productId, long categoryId);
	
	int countCategoriesWithProduct(int productId, long categoryId);
	
	void delete(long id);

	StocktakingProduct updateQuantity(long id, double quantity);
	
	StocktakingProduct save(StocktakingProduct stocktakingProduct);
}
