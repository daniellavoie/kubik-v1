package com.cspinformatique.kubik.purchase.service;

import com.cspinformatique.kubik.purchase.model.PurchaseSession;

public interface PurchaseSessionService {
	public Iterable<PurchaseSession> findAll();

	public PurchaseSession findOne(int id);
	
	public PurchaseSession save(PurchaseSession purchaseSession);
}
