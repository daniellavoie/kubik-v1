package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.purchase.Rma;

public interface RmaRepository extends JpaRepository<Rma, Integer>{
	@Query("SELECT id FROM Rma rma WHERE id > ?1")
	Page<Integer> findIdByIdGreaterThan(int id, Pageable pageable);

	@Query("SELECT id FROM Rma rma WHERE id < ?1")
	Page<Integer> findIdByIdLessThan(int id, Pageable pageable);
	
	@Query("SELECT sum(detail.quantity) FROM RmaDetail detail WHERE detail.product.id = ?1 AND detail.rma.status = 'SHIPPED'")
	Double findProductQuantityReturnedToSupplier(int productId);
}
