package com.cspinformatique.kubik.domain.sales.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {

	List<InvoiceDetail> findByProduct(Product product);

	@Query("SELECT detail FROM InvoiceDetail detail WHERE detail.product = ?1 AND detail.invoice.status = ?2")
	Page<InvoiceDetail> findByProductAndInvoiceStatus(Product product, InvoiceStatus status, Pageable pageable);
}
