package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSessionDetail;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession.Status;
import com.cspinformatique.kubik.model.product.Product;

public interface PurchaseSessionDetailRepository extends
		JpaRepository<PurchaseSessionDetail, Integer> {

	@Query("SELECT purchaseSession FROM PurchaseSessionDetail detail WHERE detail.product = ?")
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	@Query("SELECT purchaseSession FROM PurchaseSessionDetail detail WHERE detail.product = ? AND detail.purchaseSession.status = ?")
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
