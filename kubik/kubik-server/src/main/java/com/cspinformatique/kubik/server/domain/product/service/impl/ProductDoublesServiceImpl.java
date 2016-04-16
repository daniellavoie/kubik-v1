package com.cspinformatique.kubik.server.domain.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.ProductDoublesService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.product.ProductDoubles;

@Service
public class ProductDoublesServiceImpl implements ProductDoublesService {
	@Resource
	ProductService productService;

	@Override
	public List<ProductDoubles> findAll() {
		return productService.findAllProductDoubles();
	}

	@Override
	public ProductDoubles findOne(String ean13) {
		return new ProductDoubles(ean13, productService.findByEan13Doubles(ean13));
	}

}
