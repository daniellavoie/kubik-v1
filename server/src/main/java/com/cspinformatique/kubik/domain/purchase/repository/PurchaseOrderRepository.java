package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.product.Supplier;

public interface PurchaseOrderRepository extends
		RevisionRepository<PurchaseOrder, Long, Integer>,
		JpaRepository<PurchaseOrder, Long> {

	List<PurchaseOrder> findByStatus(Status status);

	List<PurchaseOrder> findBySupplierAndStatus(Supplier supplier, Status status);

	List<PurchaseOrder> findByTotalAmountTaxOut(Double totalAmountTaxOut);
}