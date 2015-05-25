package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.product.Supplier;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder.Status;

public interface PurchaseOrderRepository extends
		JpaRepository<PurchaseOrder, Long> {

	List<PurchaseOrder> findByStatus(Status status);

	List<PurchaseOrder> findBySupplierAndStatus(Supplier supplier, Status status);

	List<PurchaseOrder> findByTotalAmountTaxOut(Double totalAmountTaxOut);
}