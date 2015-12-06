package com.cspinformatique.kubik.server.domain.product.service;

import com.cspinformatique.kubik.server.model.product.Supplier;

public interface SupplierService {
	
	public Iterable<Supplier> findAll();
	
	public Supplier findByEan13(String ean13);
	
	public Supplier generateSupplierIfNotFound(String ean13);
	
	public Supplier save(Supplier supplier);
}
