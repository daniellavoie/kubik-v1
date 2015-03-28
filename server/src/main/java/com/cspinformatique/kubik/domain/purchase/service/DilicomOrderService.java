package com.cspinformatique.kubik.domain.purchase.service;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;

public interface DilicomOrderService {
	void confirmDilicomOrder(String orderFileName);
	
	public String sendOrderToDilicom(PurchaseOrder purchaseOrder);
}
