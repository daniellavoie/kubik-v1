package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.product.Supplier;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder.Status;

public interface PurchaseOrderRepository extends
		JpaRepository<PurchaseOrder, Long>, PurchaseOrderRepositoryCustom {
	
	List<PurchaseOrder> findByStatus(Status status);

	List<PurchaseOrder> findBySupplierAndStatus(Supplier supplier, Status status);

	List<PurchaseOrder> findByTotalAmountTaxOut(Double totalAmountTaxOut);
}