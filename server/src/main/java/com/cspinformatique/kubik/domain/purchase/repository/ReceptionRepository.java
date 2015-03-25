package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.Reception;
import com.cspinformatique.kubik.domain.purchase.model.Reception.Status;

public interface ReceptionRepository extends
		PagingAndSortingRepository<Reception, Integer>, RevisionRepository<Reception, Integer, Integer> {
	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);

	Page<Reception> findByStatus(Status status, Pageable pageable);
	
	@Query("SELECT sum(detail.quantityReceived) FROM ReceptionDetail detail WHERE detail.product.id = :productId AND detail.reception.status = 'CLOSED'")
	Double findProductQuantityReceived(@Param("productId") int productId);
}
