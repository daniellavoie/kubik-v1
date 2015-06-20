package com.cspinformatique.kubik.domain.product.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.Supplier;

public interface ProductService {
	Product buildProductFromReference(Reference reference);

	Iterable<Product> findByEan13(String ean13);
	
	Product findByEan13AndSupplier(String ean13, Supplier supplier);
	
	Iterable<Product> findBySupplier(Supplier supplier);
	
	Page<Product> findAll(Pageable pageable);
	
	Product findOne(int id);
	
	Set<String> getProductIdsCache();
	
	void mergeProduct(Product sourceProduct, Product targetProduct);
	
	Product save(Product product);
	
	Page<Product> search(String query, Pageable pageable);
}
