package com.daniellavoie.kubik.reporting.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.daniellavoie.kubik.reporting.model.ProductSale;

public interface ProductSaleService {
	List<ProductSale> findAll();
	
	void update(Invoice invoice);
}
