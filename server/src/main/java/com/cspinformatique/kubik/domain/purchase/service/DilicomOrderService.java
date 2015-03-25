package com.cspinformatique.kubik.domain.purchase.service;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;

public interface DilicomOrderService {
	public String sendOrderToDilicom(PurchaseOrder purchaseOrder);
}
