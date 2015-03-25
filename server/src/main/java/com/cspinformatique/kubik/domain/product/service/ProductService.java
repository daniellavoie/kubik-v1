package com.cspinformatique.kubik.domain.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.reference.model.Reference;
import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;

public interface ProductService {
	
	Product buildProductFromReference(Reference reference);

	Product findByEan13AndSupplier(String ean13, Supplier supplier);
	
	Iterable<Product> findBySupplier(Supplier supplier);
	
	Page<Product> findAll(Pageable pageable);
	
	Product findOne(int id);
	
	Product generateProductIfNotFound(String ean13, String supplierEan13);
	
	Product save(Product product);
	
	Page<Product> search(String query, Pageable pageable);
}
