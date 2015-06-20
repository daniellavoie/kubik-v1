package com.cspinformatique.kubik.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.RmaDetail;
import com.cspinformatique.kubik.model.purchase.Rma.Status;

public interface RmaDetailService {
	
	List<RmaDetail> findByProduct(Product product);
	
	Page<RmaDetail> findByProductAndRmaStatus(Product product, Status status, Pageable pageable);
	
	RmaDetail save(RmaDetail rmaDetail);
}
