package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.model.purchase.PurchaseSession.Status;

public interface PurchaseSessionRepository extends
		PagingAndSortingRepository<PurchaseSession, Integer> {
	Page<PurchaseSession> findByStatus(Status status, Pageable pageable);

	@Query("SELECT purchaseSession FROM PurchaseSession purchaseSession WHERE status in :status")
	Page<PurchaseSession> findByStatus(@Param("status") List<Status> status,
			Pageable pageable);
}
