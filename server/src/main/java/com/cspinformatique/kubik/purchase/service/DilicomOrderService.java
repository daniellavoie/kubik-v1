package com.cspinformatique.kubik.purchase.service;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;

public interface DilicomOrderService {
	public String sendOrderToDilicom(PurchaseOrder purchaseOrder);
}
