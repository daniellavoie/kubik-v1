package com.cspinformatique.kubik.server.domain.purchase.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.Reception;
import com.cspinformatique.kubik.server.model.purchase.Reception.Status;

public interface ReceptionService {
	Iterable<Reception> findAll(Pageable pageable);
	
	List<Reception> findByStatusAndDateReceivedAfter(Status status, Date dateReceived);
	
	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);
	
	Reception findOne(int id);
	
	double findProductQuantityReceived(int productId);
	
	void initialize();

	Reception save(Reception reception);
	
	Iterable<Reception> save(Iterable<Reception> receptions);
	
	void validate();
}
