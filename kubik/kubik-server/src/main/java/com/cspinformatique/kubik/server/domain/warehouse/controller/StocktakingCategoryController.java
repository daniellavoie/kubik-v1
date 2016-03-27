package com.cspinformatique.kubik.server.domain.warehouse.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingCategoryService;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingCategory;

@Controller
@RequestMapping("/stocktaking-category")
public class StocktakingCategoryController {
	@Resource
	private StocktakingCategoryService stocktakingCategoryService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public StocktakingCategory save(@RequestBody StocktakingCategory stocktakingCategory) {
		return stocktakingCategoryService.save(stocktakingCategory);
	}
}
