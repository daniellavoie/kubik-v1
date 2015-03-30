package com.cspinformatique.kubik.domain.product.service;

import com.cspinformatique.kubik.model.product.Supplier;

public interface SupplierService {
	
	public Iterable<Supplier> findAll();
	
	public Supplier findByEan13(String ean13);
	
	public Supplier generateSupplierIfNotFound(String ean13);
	
	public Supplier save(Supplier supplier);
}
