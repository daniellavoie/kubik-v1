package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.product.Product;

public interface PurchaseOrderDetailRepository extends
		JpaRepository<PurchaseOrderDetail, Integer> {
	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = ?")
	List<PurchaseOrder> findPurchaseOrdersByProduct(Product product);
	
	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = ? AND detail.purchaseOrder.status = ?")
	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
