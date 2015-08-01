package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.Reception.Status;
import com.cspinformatique.kubik.model.purchase.ReceptionDetail;

public interface ReceptionDetailRepository extends
		PagingAndSortingRepository<ReceptionDetail, Integer> {
	
	List<ReceptionDetail> findByProduct(Product product);
	
	@Query("SELECT detail FROM ReceptionDetail detail WHERE detail.product = ?1 AND detail.reception.status = ?2")
	Page<ReceptionDetail> findByProductAndReceptionStatus(
			Product product, Status status,
			Pageable pageable);
}
