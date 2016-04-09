package com.cspinformatique.kubik.server.domain.warehouse.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingService;
import com.cspinformatique.kubik.server.model.warehouse.Stocktaking;

@Controller
@RequestMapping(value = "/stocktaking")
public class StocktakingController {
	@Resource
	private StocktakingService stocktakingService;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "adjust")
	public void applyInventoryAdjustments(@PathVariable long id) {
		stocktakingService.applyInventoryAdjustments(id);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Stocktaking> findAll() {
		return stocktakingService.findAll();
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Stocktaking findOne(@PathVariable long id) {
		return stocktakingService.findOne(id);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "generateDiffs")
	public void generateStocktakingDiffs(@PathVariable long id) {
		stocktakingService.generateStocktakingDiffs(id);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = "dummy")
	public Stocktaking generateDummyStocktaking() {
		return stocktakingService.generateDummyStocktaking();
	}

	@RequestMapping(value = "/{id}/stocktaking-category/{stocktakingId}", produces = MediaType.TEXT_HTML_VALUE)
	public String getStocktakingCategoryPage() {
		return "warehouse/stocktaking-category";
	}

	@RequestMapping(value = "/{id}/stocktaking-diffs", produces = MediaType.TEXT_HTML_VALUE)
	public String getStocktakingDiffsPage() {
		return "warehouse/stocktaking-diffs";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getStocktakingPage() {
		return "warehouse/stocktaking";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getStocktakingsPage() {
		return "warehouse/stocktakings";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public Stocktaking save(@RequestBody Stocktaking stocktaking) {
		return stocktakingService.save(stocktaking);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "updateInventory")
	public Stocktaking updateInventory(@PathVariable long id) {
		return stocktakingService.updateInventory(id);
	}
}