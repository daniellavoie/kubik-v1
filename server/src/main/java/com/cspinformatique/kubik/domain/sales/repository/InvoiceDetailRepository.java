package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;

public interface InvoiceDetailRepository extends
		JpaRepository<InvoiceDetail, Integer> {
	@Query("SELECT detail FROM InvoiceDetail detail WHERE detail.product = :product AND detail.invoice.status = :status")
	Page<InvoiceDetail> findByProductAndInvoiceStatus(
			@Param("product") Product product, @Param("status") InvoiceStatus status,
			Pageable pageable);
}
