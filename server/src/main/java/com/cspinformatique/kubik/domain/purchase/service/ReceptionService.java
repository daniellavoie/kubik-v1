package com.cspinformatique.kubik.domain.purchase.service;

import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.Reception;

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
