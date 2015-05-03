package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.domain.purchase.model.Reception.Status;
import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.model.product.Product;

public interface ReceptionDetailRepository extends
		PagingAndSortingRepository<ReceptionDetail, Integer> {
	@Query("SELECT detail FROM ReceptionDetail detail WHERE detail.product = :product AND detail.reception.status = :status")
	Page<ReceptionDetail> findByProductAndReceptionStatus(
			@Param("product") Product product, @Param("status") Status status,
			Pageable pageable);
}
