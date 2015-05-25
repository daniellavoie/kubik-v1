package com.cspinformatique.kubik.domain.purchase.service;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.model.purchase.PurchaseSession.Status;

public interface PurchaseSessionDetailService {
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
