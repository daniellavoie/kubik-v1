package com.cspinformatique.kubik.server.domain.product.service;

import com.cspinformatique.kubik.server.model.product.Supplier;

public interface SupplierService {
	
	Iterable<Supplier> findAll();
	
	Supplier findByEan13(String ean13);
	
	Supplier findOne(Integer id);
	
	Supplier generateSupplierIfNotFound(String ean13);
	
	Supplier save(Supplier supplier);
}
