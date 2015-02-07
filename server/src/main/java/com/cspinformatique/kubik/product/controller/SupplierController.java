package com.cspinformatique.kubik.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.product.service.SupplierService;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Supplier> findAll() {
		return this.supplierService.findAll();
	}

	@RequestMapping(params = "card", produces = MediaType.TEXT_HTML_VALUE)
	public String getSupplierCardPage() {
		return "product/supplier-card :: supplier-card";
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getSupplierPage() {
		return "product/suppliers";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Supplier save(@RequestBody Supplier supplier) {
		return this.supplierService.save(supplier);
	}
}
