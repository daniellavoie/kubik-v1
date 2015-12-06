package com.cspinformatique.kubik.server.domain.purchase.service;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSessionDetail;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSession.Status;

public interface PurchaseSessionDetailService {
	Iterable<PurchaseSessionDetail> findByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
	
	PurchaseSessionDetail save(PurchaseSessionDetail purchaseSessionDetail);
}
