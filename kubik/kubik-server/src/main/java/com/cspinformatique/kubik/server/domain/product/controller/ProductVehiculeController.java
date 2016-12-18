package com.cspinformatique.kubik.server.domain.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product-vehicule")
public class ProductVehiculeController {
	@GetMapping
	public String getProductVehiculePage(){
		return "product-vehicule/products-page";
	}
}
