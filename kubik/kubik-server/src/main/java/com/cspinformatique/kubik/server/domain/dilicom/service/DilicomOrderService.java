package com.cspinformatique.kubik.server.domain.dilicom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.dilicom.DilicomOrder;

public interface DilicomOrderService {
	Page<DilicomOrder> findAll(Pageable pageable);
	
	DilicomOrder save(DilicomOrder dilicomOrder);
	
	void sendPendingDilicomOrders();
}
