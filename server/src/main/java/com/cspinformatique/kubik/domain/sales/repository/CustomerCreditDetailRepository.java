package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;

public interface CustomerCreditDetailRepository extends
		JpaRepository<CustomerCreditDetail, Integer> {
	@Query("SELECT detail FROM CustomerCreditDetail detail WHERE detail.product = :product AND detail.customerCredit.status = :status")
	Page<CustomerCreditDetail> findByProductAndInvoiceStatus(
			@Param("product") Product product, @Param("status") Status status,
			Pageable pageable);
}
