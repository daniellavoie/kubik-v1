package com.cspinformatique.kubik.product.service;

import com.cspinformatique.kubik.product.model.Supplier;

public interface SupplierService {
	
	public Iterable<Supplier> findAll();
	
	public Supplier findByEan13(String ean13);
	
	public Supplier generateSupplierIfNotFound(String ean13);
	
	public Supplier save(Supplier supplier);
}
