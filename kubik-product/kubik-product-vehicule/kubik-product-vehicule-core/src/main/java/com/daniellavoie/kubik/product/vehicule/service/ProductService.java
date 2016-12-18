package com.daniellavoie.kubik.product.vehicule.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.daniellavoie.kubik.product.vehicule.model.Product;

public interface ProductService {
	void delete(String ean13);
	
	Product findOne(String ean13);
	
	Page<Product> query(String queryString, Pageable pageable);
	
	Product save(Product product);
} 
