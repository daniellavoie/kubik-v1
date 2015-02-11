package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.purchase.model.PurchaseOrderDetail;

public interface PurchaseOrderDetailRepository extends
		JpaRepository<PurchaseOrderDetail, Integer> {
	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = ?")
	Iterable<PurchaseOrder> findPurchaseOrdersByProduct(Product product);
	
	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = ? AND detail.purchaseOrder.status = ?")
	Iterable<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
