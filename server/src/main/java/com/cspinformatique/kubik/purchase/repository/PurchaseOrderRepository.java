package com.cspinformatique.kubik.purchase.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder.Status;

public interface PurchaseOrderRepository extends
		PagingAndSortingRepository<PurchaseOrder, Long> {
	
	List<PurchaseOrder> findByStatus(Status status);
	
	List<PurchaseOrder> findBySupplierAndStatus(Supplier supplier, Status status);
	
	List<PurchaseOrder> findByTotalAmountTaxOut(Double totalAmountTaxOut);
}