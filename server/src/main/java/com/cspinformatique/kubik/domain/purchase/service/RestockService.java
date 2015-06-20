package com.cspinformatique.kubik.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.Restock;
import com.cspinformatique.kubik.model.purchase.Restock.Status;

public interface RestockService {
	
	Long countByStatus(Status status);
	
	List<Restock> findByProduct(Product product);
	
	Page<Restock> findByStatus(Status status, Pageable pageable);
	
	Restock findOne(int id);
	
	Restock restockProduct(Product product, double quantity);
	
	Restock save(Restock restock);
}
