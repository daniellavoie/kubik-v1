package com.cspinformatique.kubik.server.domain.warehouse.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;

public interface StocktakingDiffService {
	void delete(StocktakingDiff stocktakingDiff);
	
	List<StocktakingDiff> findByProduct(Product product);
	
	StocktakingDiff updateCountedQuantity(long id, double quantity);
	
	StocktakingDiff updateValidated(long id, boolean validated);
}
