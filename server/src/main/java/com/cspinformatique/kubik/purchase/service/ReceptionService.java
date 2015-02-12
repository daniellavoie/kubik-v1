package com.cspinformatique.kubik.purchase.service;

import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.Reception;

public interface ReceptionService {
	Iterable<Reception> findAll(Pageable pageable);
	
	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);
	
	Reception findOne(int id);
	
	void initialize();

	Reception save(Reception reception);
}
