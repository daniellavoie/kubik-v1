package com.cspinformatique.kubik.purchase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.model.PurchaseSession.Status;

public interface PurchaseSessionService {
	
	Page<PurchaseSession> findByStatus(Status status, Pageable pageable);
	
	Iterable<PurchaseSession> findAll();
	
	Page<PurchaseSession> findAll(Pageable pageable);

	PurchaseSession findOne(int id);
	
	PurchaseSession save(PurchaseSession purchaseSession);
}
