package com.cspinformatique.kubik.server.domain.warehouse.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.warehouse.repository.StocktakingDiffRepository;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingDiffService;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;

@Service
public class StocktakingDiffServiceImpl implements StocktakingDiffService {
	@Resource
	private StocktakingDiffRepository stocktakingDiffRepository;

	private StocktakingDiff findOne(long id) {
		return stocktakingDiffRepository.findOne(id);
	}

	@Override
	public StocktakingDiff updateAdjustmentQuantity(long id, double adjustmentQuantity) {
		StocktakingDiff stocktakingDiff = findOne(id);

		stocktakingDiff.setAdjustmentQuantity(adjustmentQuantity);

		return save(stocktakingDiff);
	}

	@Override
	public StocktakingDiff updateValidated(long id, boolean validated) {
		StocktakingDiff stocktakingDiff = findOne(id);

		stocktakingDiff.setValidated(validated);

		return save(stocktakingDiff);
	}

	private StocktakingDiff save(StocktakingDiff stocktakingDiff) {
		return stocktakingDiffRepository.save(stocktakingDiff);
	}
}
