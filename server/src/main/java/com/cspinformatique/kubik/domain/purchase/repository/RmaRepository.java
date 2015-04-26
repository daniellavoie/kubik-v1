package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.domain.purchase.model.Rma;

public interface RmaRepository extends JpaRepository<Rma, Integer>{
	@Query("SELECT id FROM Rma rma WHERE id > :id")
	Page<Integer> findIdByIdGreaterThan(@Param("id") int id, Pageable pageable);

	@Query("SELECT id FROM Rma rma WHERE id < :id")
	Page<Integer> findIdByIdLessThan(@Param("id") int id, Pageable pageable);
	
	@Query("SELECT sum(detail.quantity) FROM RmaDetail detail WHERE detail.product.id = :productId AND detail.rma.status = 'SHIPPED'")
	Double findProductQuantityReturnedToSupplier(@Param("productId") int productId);
}
