package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.model.PurchaseSession.Status;
import com.cspinformatique.kubik.purchase.model.PurchaseSessionDetail;

public interface PurchaseSessionDetailRepository extends
		JpaRepository<PurchaseSessionDetail, Integer> {

	@Query("SELECT purchaseSession FROM PurchaseSessionDetail detail WHERE detail.product = ?")
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	@Query("SELECT purchaseSession FROM PurchaseSessionDetail detail WHERE detail.product = ? AND detail.purchaseSession.status = ?")
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
