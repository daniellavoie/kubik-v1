package com.daniellavoie.kubik.product.vehicule.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daniellavoie.kubik.product.vehicule.model.Product;
import com.daniellavoie.kubik.product.vehicule.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping(value = "/{ean13}")
	public Product findOne(@PathVariable String ean13) {
		return productService.findOne(ean13);
	}

	@GetMapping
	public Page<Product> query(@RequestParam(required = false) String queryString, Pageable pageable) {
		return productService.query(queryString, pageable);
	}

	@PostMapping
	public Product save(@RequestBody Product product) {
		return productService.save(product);
	}
}
