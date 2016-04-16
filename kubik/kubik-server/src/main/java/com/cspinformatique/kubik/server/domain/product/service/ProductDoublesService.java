package com.cspinformatique.kubik.server.domain.product.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.product.ProductDoubles;

public interface ProductDoublesService {
	List<ProductDoubles> findAll();

	ProductDoubles findOne(String ean13);
}
