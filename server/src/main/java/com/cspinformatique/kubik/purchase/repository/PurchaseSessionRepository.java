
package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.model.PurchaseSession.Status;

public interface PurchaseSessionRepository extends
		PagingAndSortingRepository<PurchaseSession, Integer> {
	Page<PurchaseSession> findByStatus(Status status, Pageable pageable);
}
