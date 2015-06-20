package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder.Status;

public interface PurchaseOrderDetailRepository extends
		JpaRepository<PurchaseOrderDetail, Integer> {
	
	List<PurchaseOrderDetail> findByProduct(Product product);
	
	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = :product")
	List<PurchaseOrder> findPurchaseOrdersByProduct(
			@Param("product") Product product);

	@Query("SELECT purchaseOrder FROM PurchaseOrderDetail detail WHERE detail.product = :product AND detail.purchaseOrder.status = :status")
	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(
			@Param("product") Product product, @Param("status") Status status);
}
