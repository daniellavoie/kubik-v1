package com.cspinformatique.kubik.domain.purchase.service;

import com.cspinformatique.kubik.model.purchase.PurchaseOrder;

public interface DilicomOrderService {
	void confirmDilicomOrder(String orderFileName);
	
	public String sendOrderToDilicom(PurchaseOrder purchaseOrder);
}
