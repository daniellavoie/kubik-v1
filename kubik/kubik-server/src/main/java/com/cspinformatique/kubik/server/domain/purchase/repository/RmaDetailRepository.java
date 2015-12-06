package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.RmaDetail;
import com.cspinformatique.kubik.server.model.purchase.Rma.Status;

public interface RmaDetailRepository extends JpaRepository<RmaDetail, Integer> {
	
	List<RmaDetail> findByProduct(Product product);	
	
	@Query("SELECT rmaDetail FROM RmaDetail rmaDetail WHERE rmaDetail.product = ?1 AND rmaDetail.rma.status = ?2" )
	Page<RmaDetail> findByProductAndRmaStatus(Product product, Status status, Pageable pageable);
}
