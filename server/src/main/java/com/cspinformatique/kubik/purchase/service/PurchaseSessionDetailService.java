package com.cspinformatique.kubik.purchase.service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.model.PurchaseSession.Status;

public interface PurchaseSessionDetailService {
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
