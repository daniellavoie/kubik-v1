package com.cspinformatique.kubik.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.reference.model.Reference;

public interface ProductService {
	
	Product buildProductFromReference(Reference reference);
	
	Product findByEan13AndSupplier(String ean13, Supplier supplier);
	
	Page<Product> findAll(Pageable pageable);
	
	Product findOne(int id);
	
	Product generateProductIfNotFound(String ean13, String supplierEan13);
	
	Product save(Product product);
	
	Page<Product> search(String query, Pageable pageable);
}
