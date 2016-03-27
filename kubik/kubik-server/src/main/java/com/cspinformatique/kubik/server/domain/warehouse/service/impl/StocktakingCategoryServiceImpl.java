package com.cspinformatique.kubik.server.domain.warehouse.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.warehouse.repository.StocktakingCategoryRepository;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingCategoryService;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingCategory;

@Service
public class StocktakingCategoryServiceImpl implements StocktakingCategoryService {
	@Resource
	private StocktakingCategoryRepository stocktakingCategoryRepository;

	@Override
	public StocktakingCategory findOne(long id) {
		return stocktakingCategoryRepository.findOne(id);
	}

	@Override
	public StocktakingCategory save(StocktakingCategory stocktakingCategory) {
		if (stocktakingCategory.getCreationDate() == null)
			stocktakingCategory.setCreationDate(new Date());

		return stocktakingCategoryRepository.save(stocktakingCategory);
	}
}
