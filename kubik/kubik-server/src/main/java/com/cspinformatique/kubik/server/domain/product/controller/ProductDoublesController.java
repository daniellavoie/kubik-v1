package com.cspinformatique.kubik.server.domain.product.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.server.domain.product.service.ProductDoublesService;
import com.cspinformatique.kubik.server.model.product.ProductDoubles;

@Controller
@RequestMapping("/product-doubles")
public class ProductDoublesController {
	@Resource
	ProductDoublesService productDoublesService;

	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDoubles> findAll() {
		return productDoublesService.findAll();
	}

	@ResponseBody
	@RequestMapping(value = "/{ean13}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductDoubles findOne(@PathVariable String ean13) {
		return productDoublesService.findOne(ean13);
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getProductDoublesListPage() {
		return "product/product-doubles/product-doubles-list";
	}

	@RequestMapping(value = "/{ean13}", produces = MediaType.TEXT_HTML_VALUE)
	public String getProductDoublesPage(@PathVariable String ean13) {
		return "product/product-doubles/product-doubles";
	}
}
