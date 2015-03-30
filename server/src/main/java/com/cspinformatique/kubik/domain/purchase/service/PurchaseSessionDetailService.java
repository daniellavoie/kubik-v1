package com.cspinformatique.kubik.domain.purchase.service;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession.Status;
import com.cspinformatique.kubik.model.product.Product;

public interface PurchaseSessionDetailService {
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
