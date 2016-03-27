package com.cspinformatique.kubik.server.domain.warehouse.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingDiffService;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;

@RestController
@RequestMapping("/stocktaking-diff")
public class StocktakingDiffController {
	@Resource
	private StocktakingDiffService stocktakingDiffService;

	@RequestMapping(value = "/{id}", params = "adjustmentQuantity")
	public StocktakingDiff updateAdjustmentQuantity(@PathVariable long id, @RequestParam double adjustmentQuantity) {
		return stocktakingDiffService.updateAdjustmentQuantity(id, adjustmentQuantity);
	}

	@RequestMapping(value = "/{id}", params = "validated")
	public StocktakingDiff updateValidated(@PathVariable long id, @RequestParam boolean validated) {
		return stocktakingDiffService.updateValidated(id, validated);
	}
}
