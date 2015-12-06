package com.cspinformatique.kubik.server.domain.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSessionDetail;
import com.cspinformatique.kubik.server.model.purchase.PurchaseSession.Status;

public interface PurchaseSessionDetailRepository extends
		JpaRepository<PurchaseSessionDetail, Integer> {

	Iterable<PurchaseSessionDetail> findByProduct(Product product);
	
	@Query("SELECT purchaseSession FROM PurchaseSessionDetail detail WHERE detail.product = ?1")
	Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product);
	
	@Query("SELECT purchaseSession FROM PurchaseSessionDetail detail WHERE detail.product = ?1 AND detail.purchaseSession.status = ?2")
	Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
