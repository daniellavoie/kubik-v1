package com.cspinformatique.kubik.server.domain.warehouse.service;

import com.cspinformatique.kubik.server.model.warehouse.StocktakingCategory;

public interface StocktakingCategoryService {
	StocktakingCategory findOne(long id);
	
	StocktakingCategory save(StocktakingCategory stocktakingCategory);
}
