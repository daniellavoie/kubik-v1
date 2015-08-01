package com.cspinformatique.kubik.domain.sales.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;

public interface CustomerCreditDetailRepository extends
		JpaRepository<CustomerCreditDetail, Integer> {
	
	List<CustomerCreditDetail> findByProduct(Product product);
	
	@Query("SELECT detail FROM CustomerCreditDetail detail WHERE detail.product = ?1 AND detail.customerCredit.status = ?2")
	Page<CustomerCreditDetail> findByProductAndInvoiceStatus(
			Product product, Status status,
			Pageable pageable);
}
