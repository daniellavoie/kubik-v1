package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.Restock;
import com.cspinformatique.kubik.model.purchase.Restock.Status;

public interface RestockRepository extends JpaRepository<Restock, Integer> {
	
	Long countByStatus(Status status);
	
	Restock findByProductAndStatus(Product product, Status status);
	
	Page<Restock> findByStatus(Status status, Pageable pageable);
}
