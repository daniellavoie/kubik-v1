package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder.Status;

public interface PurchaseOrderDetailRepository extends
		JpaRepository<PurchaseOrderDetail, Integer> {
	
	List<PurchaseOrderDetail> findByProduct(Product product);
	
	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = ?1")
	List<PurchaseOrder> findPurchaseOrdersByProduct(
			Product product);

	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = ?1 AND detail.purchaseOrder.status = ?2")
	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(
			Product product, Status status);
}
