package com.cspinformatique.kubik.domain.purchase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.purchase.model.RmaDetail;
import com.cspinformatique.kubik.domain.purchase.model.Rma.Status;
import com.cspinformatique.kubik.model.product.Product;

public interface RmaDetailService {
	public Page<RmaDetail> findByProductAndRmaStatus(Product product, Status status, Pageable pageable);
}
