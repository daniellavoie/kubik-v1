package com.cspinformatique.kubik.server.domain.purchase.service;

import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.Reception;

public interface ReceptionService {
	Iterable<Reception> findAll(Pageable pageable);
	
	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);
	
	Reception findOne(int id);
	
	double findProductQuantityReceived(int productId);
	
	void initialize();

	Reception save(Reception reception);
	
	Iterable<Reception> save(Iterable<Reception> receptions);
	
	void validate();
}
