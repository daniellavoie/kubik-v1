package com.cspinformatique.kubik.server.domain.warehouse.service;

import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;

public interface StocktakingDiffService {
	StocktakingDiff updateAdjustmentQuantity(long id, double quantity);
	
	StocktakingDiff updateValidated(long id, boolean validated);
}
