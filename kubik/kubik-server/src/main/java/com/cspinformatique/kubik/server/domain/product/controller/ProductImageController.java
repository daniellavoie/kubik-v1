package com.cspinformatique.kubik.server.domain.product.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.product.service.ProductImageService;

@Controller
@RequestMapping("/productImage")
public class ProductImageController {
	@Resource
	private ProductImageService productImageService;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(params = "initialLoad")
	public void initialLoad() {
		productImageService.initialLoad();
	}
}
