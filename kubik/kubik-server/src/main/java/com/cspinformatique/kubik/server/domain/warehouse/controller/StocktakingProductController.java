package com.cspinformatique.kubik.server.domain.warehouse.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingProductService;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingProduct;

@Controller
@RequestMapping({ "/stocktaking-product",
		"/stocktaking/{stocktakingId}/stocktakingCategory/{stocktakingCategoryId}/stocktakingProduct" })
public class StocktakingProductController {
	@Resource
	private StocktakingProductService stocktakingProductService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, params = "productId")
	public StocktakingProduct addProductToCategory(@RequestParam int productId,
			@PathVariable long stocktakingCategoryId) {
		return stocktakingProductService.addProductToCategory(productId, stocktakingCategoryId);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable long id) {
		stocktakingProductService.delete(id);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = "productId")
	public int countCategoriesWithProduct(@RequestParam int productId, @PathVariable long stocktakingCategoryId) {
		return stocktakingProductService.countCategoriesWithProduct(productId, stocktakingCategoryId);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "quantity")
	public StocktakingProduct updateQuantity(@PathVariable long id, @RequestParam double quantity) {
		return stocktakingProductService.updateQuantity(id, quantity);
	}
}
