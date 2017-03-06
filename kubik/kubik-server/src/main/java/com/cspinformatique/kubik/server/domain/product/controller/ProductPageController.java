package com.cspinformatique.kubik.server.domain.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductPageController {
	@GetMapping(params = { "random", "category", "page" })
	public String getNonCategorizedProductsPage() {
		return "product/non-categorized-product";
	}

	@GetMapping(params = "card")
	public String getProductCardPage() {
		return "product/product-card :: product-card";
	}

	@GetMapping
	public String getProductsPage() {
		return "product/products-page";
	}

	@GetMapping(params = { "random", "nonValidatedProductImages", "page" })
	public String getNonValidatedProductImagesPage() {
		return "product/non-validated-product-images";
	}
}
