package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.Rma.Status;
import com.cspinformatique.kubik.model.purchase.RmaDetail;

public interface RmaDetailRepository extends JpaRepository<RmaDetail, Integer> {
	
	List<RmaDetail> findByProduct(Product product);	
	
	@Query("SELECT rmaDetail FROM RmaDetail rmaDetail WHERE rmaDetail.product = ?1 AND rmaDetail.rma.status = ?2" )
	Page<RmaDetail> findByProductAndRmaStatus(Product product, Status status, Pageable pageable);
}
