package com.cspinformatique.kubik.server.domain.warehouse.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.warehouse.Stocktaking;

public interface StocktakingService {

	List<Stocktaking> findAll();

	Stocktaking findOne(long id);

	void generateDummyStocktaking();

	void generateStocktakingDiffs(long id);
	
	Stocktaking save(Stocktaking stocktaking);
	
	Stocktaking updateInventory(long stocktakingId);
}
