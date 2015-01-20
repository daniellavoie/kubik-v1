
package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.purchase.model.PurchaseSession;

public interface PurchaseSessionRepository extends
		PagingAndSortingRepository<PurchaseSession, Integer> {

}
