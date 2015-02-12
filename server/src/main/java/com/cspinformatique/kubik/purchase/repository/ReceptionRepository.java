package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.Reception;

public interface ReceptionRepository extends
		PagingAndSortingRepository<Reception, Integer> {
	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
