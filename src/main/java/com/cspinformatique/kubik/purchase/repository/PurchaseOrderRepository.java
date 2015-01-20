package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;

public interface PurchaseOrderRepository extends
		PagingAndSortingRepository<PurchaseOrder, Long> {

}
