package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.Reception;
import com.cspinformatique.kubik.server.model.purchase.Reception.Status;

public interface ReceptionRepository extends PagingAndSortingRepository<Reception, Integer> {

	Reception findByPurchaseOrder(PurchaseOrder purchaseOrder);

	Page<Reception> findByStatus(Status status, Pageable pageable);

	List<Reception> findByStatusAndDateReceivedAfter(Status status, Date dateReceived);

	@Query("SELECT sum(detail.quantityReceived) FROM ReceptionDetail detail WHERE detail.product.id = ?1 AND detail.reception.status = 'CLOSED'")
	Double findProductQuantityReceived(int productId);

	@Query("SELECT sum(detail.quantityReceived) FROM ReceptionDetail detail WHERE detail.product.id = ?1 AND detail.reception.status = 'CLOSED' AND detail.reception.dateReceived < ?2")
	Double findProductQuantityReceivedUntil(int productId, Date until);
}
