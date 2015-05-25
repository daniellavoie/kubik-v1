package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.RmaDetail;
import com.cspinformatique.kubik.model.purchase.Rma.Status;

public interface RmaDetailRepository extends JpaRepository<RmaDetail, Integer> {
	@Query("SELECT rmaDetail FROM RmaDetail rmaDetail WHERE rmaDetail.product = :product AND rmaDetail.rma.status = :status" )
	Page<RmaDetail> findByProductAndRmaStatus(@Param("product") Product product, @Param("status") Status status, Pageable pageable);
}
