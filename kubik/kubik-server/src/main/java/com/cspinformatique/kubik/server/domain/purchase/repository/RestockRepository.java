package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.Restock;
import com.cspinformatique.kubik.server.model.purchase.Restock.Status;

public interface RestockRepository extends JpaRepository<Restock, Integer> {
	
	Long countByStatus(Status status);
	
	List<Restock> findByProduct(Product product);
	
	Restock findByProductAndStatus(Product product, Status status);
	
	Page<Restock> findByStatus(Status status, Pageable pageable);
}
