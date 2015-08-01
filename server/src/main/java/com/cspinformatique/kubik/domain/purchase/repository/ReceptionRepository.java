package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.Reception;
import com.cspinformatique.kubik.model.purchase.Reception.Status;

public interface ReceptionRepository extends
		PagingAndSortingRepository<Reception, Integer> {
	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);

	Page<Reception> findByStatus(Status status, Pageable pageable);
	
	@Query("SELECT sum(detail.quantityReceived) FROM ReceptionDetail detail WHERE detail.product.id = ?1 AND detail.reception.status = 'CLOSED'")
	Double findProductQuantityReceived(int productId);
}
