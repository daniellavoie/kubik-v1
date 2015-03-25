package com.cspinformatique.kubik.domain.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.product.service.SupplierService;
import com.cspinformatique.kubik.product.model.Product;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private SupplierService supplierService;

	@RequestMapping(params = { "ean13", "supplierEan13" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Product findByEan13AndSupplierEan13(
			@RequestParam String ean13, @RequestParam String supplierEan13) {
		return this.productService.findByEan13AndSupplier(ean13,
				this.supplierService.findByEan13(supplierEan13));
	}

	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Product findOne(@PathVariable int id) {
		return this.productService.findOne(id);
	}

	@RequestMapping(params = "card", produces = MediaType.TEXT_HTML_VALUE)
	public String getProductCardPage() {
		return "product/product-card :: product-card";
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getProductsPage() {
		return "product/products-page";
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Product save(@RequestBody Product product) {
		return this.productService.save(product);
	}

	@RequestMapping(method = RequestMethod.GET, params = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Product> search(@RequestParam String query,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "extendedLabel") String sortBy) {
		return this.productService.search(query, new PageRequest(page,
				resultPerPage, direction != null ? direction : Direction.ASC,
				sortBy));
	}
}
