package com.cspinformatique.kubik.domain.purchase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession.Status;
import com.cspinformatique.kubik.product.model.Product;

public interface PurchaseSessionService {
	
	Iterable<PurchaseSession> findAll();
	
	Page<PurchaseSession> findAll(Pageable pageable);
	
	Page<PurchaseSession> findByStatus(Status status, Pageable pageable);
	
	Iterable<PurchaseSession> findByProduct(Product product);
	
	Iterable<PurchaseSession> findByProductAndStatus(Product product, Status status);

	PurchaseSession findOne(int id);
	
	PurchaseSession save(PurchaseSession purchaseSession);
}
