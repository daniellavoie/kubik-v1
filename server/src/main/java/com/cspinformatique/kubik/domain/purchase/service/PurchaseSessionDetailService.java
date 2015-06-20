package com.cspinformatique.kubik.domain.purchase.service;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.model.purchase.PurchaseSession.Status;
import com.cspinformatique.kubik.model.purchase.PurchaseSessionDetail;

public interface PurchaseSessionDetailService {
	Iterable<PurchaseSessionDetail> findByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
	
	PurchaseSessionDetail save(PurchaseSessionDetail purchaseSessionDetail);
}
