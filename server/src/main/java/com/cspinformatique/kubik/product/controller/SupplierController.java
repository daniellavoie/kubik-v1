package com.cspinformatique.kubik.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.product.service.SupplierService;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	@Autowired private SupplierService supplierService;
	
	@RequestMapping
	public @ResponseBody Iterable<Supplier> findAll(){
		return this.supplierService.findAll();
	}
}
