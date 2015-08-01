package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.purchase.PurchaseOrderDetail;

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
