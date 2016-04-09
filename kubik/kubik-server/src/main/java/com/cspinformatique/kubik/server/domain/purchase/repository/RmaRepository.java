package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.purchase.Rma.Status;

public interface RmaRepository extends JpaRepository<Rma, Integer> {
	List<Rma> findByStatusAndShippedDateAfter(Status status, Date shippedDate);

	@Query("SELECT id FROM Rma rma WHERE id > ?1")
	Page<Integer> findIdByIdGreaterThan(int id, Pageable pageable);

	@Query("SELECT id FROM Rma rma WHERE id < ?1")
	Page<Integer> findIdByIdLessThan(int id, Pageable pageable);

	@Query("SELECT sum(detail.quantity) FROM RmaDetail detail WHERE detail.product.id = ?1 AND detail.rma.status = 'SHIPPED'")
	Double findProductQuantityReturnedToSupplier(int productId);

	@Query("SELECT sum(detail.quantity) FROM RmaDetail detail WHERE detail.product.id = ?1 AND detail.rma.status = 'SHIPPED' and detail.rma.shippedDate < ?2")
	Double findProductQuantityReturnedToSupplierUntil(int productId, Date until);
}
