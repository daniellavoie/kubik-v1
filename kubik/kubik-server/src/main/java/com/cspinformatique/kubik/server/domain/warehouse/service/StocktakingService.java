package com.cspinformatique.kubik.server.domain.warehouse.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking;

public interface StocktakingService {

	void applyInventoryAdjustments(long id);

	void applyInventoryAdjustments(Product product, double addedQuantity);

	List<Stocktaking> findAll();

	Stocktaking findOne(long id);

	Stocktaking generateDummyStocktaking();

	void generateStocktakingDiffs(long id);

	Stocktaking save(Stocktaking stocktaking);

	Stocktaking updateInventory(long stocktakingId);
}
