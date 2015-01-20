package com.cspinformatique.kubik.purchase.service;

import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.Reception;

public interface PurchaseOrderService {
	public Iterable<PurchaseOrder> findAll();
	
	public Iterable<PurchaseOrder> findAll(Pageable pageable);
	
	public PurchaseOrder findOne(long id);
	
	public Reception generateReception(PurchaseOrder purchaseOrder);
	
	public PurchaseOrder save(PurchaseOrder purchaseOrder);
	
	public Iterable<PurchaseOrder> save(Iterable<PurchaseOrder> entities);
}
