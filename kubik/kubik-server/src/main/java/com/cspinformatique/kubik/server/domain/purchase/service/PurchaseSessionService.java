package com.cspinformatique.kubik.server.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSession.Status;

public interface PurchaseSessionService {
	PurchaseSession addProductToNewestPurchaseSession(Product product, double quantity);
	
	Iterable<PurchaseSession> findAll();
	
	Page<PurchaseSession> findAll(Pageable pageable);
	
	Page<PurchaseSession> findByStatus(Status status, Pageable pageable);
	
	Page<PurchaseSession> findByStatus(List<Status> status, Pageable pageable);
	
	Iterable<PurchaseSession> findByProduct(Product product);
	
	Iterable<PurchaseSession> findByProductAndStatus(Product product, Status status);

	PurchaseSession findOne(int id);
	
	PurchaseSession save(PurchaseSession purchaseSession);
}
