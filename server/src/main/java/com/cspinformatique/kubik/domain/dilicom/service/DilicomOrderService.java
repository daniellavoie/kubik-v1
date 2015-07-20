package com.cspinformatique.kubik.domain.dilicom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.dilicom.DilicomOrder;

public interface DilicomOrderService {
	Page<DilicomOrder> findAll(Pageable pageable);
	
	DilicomOrder save(DilicomOrder dilicomOrder);
	
	void sendPendingDilicomOrders();
}
