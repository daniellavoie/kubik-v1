package com.cspinformatique.kubik.domain.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;

public interface InvoiceDetailService {
	Page<InvoiceDetail> findByProductAndInvoiceStatus(Product product,
			InvoiceStatus status, Pageable pageable);
}
