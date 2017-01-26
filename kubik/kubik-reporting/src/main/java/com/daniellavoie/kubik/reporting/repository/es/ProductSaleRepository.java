package com.daniellavoie.kubik.reporting.repository.es;

import java.util.List;

import com.daniellavoie.kubik.reporting.model.ProductSale;

public interface ProductSaleRepository {
	List<ProductSale> findAll();

	void save(List<ProductSale> productSales);
}
