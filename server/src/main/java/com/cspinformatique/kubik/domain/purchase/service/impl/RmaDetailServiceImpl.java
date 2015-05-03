package com.cspinformatique.kubik.domain.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.model.Rma.Status;
import com.cspinformatique.kubik.domain.purchase.model.RmaDetail;
import com.cspinformatique.kubik.domain.purchase.repository.RmaDetailRepository;
import com.cspinformatique.kubik.domain.purchase.service.RmaDetailService;
import com.cspinformatique.kubik.model.product.Product;

@Service
public class RmaDetailServiceImpl implements RmaDetailService {
	@Autowired
	private RmaDetailRepository rmaDetailRepository;
	
	@Override
	public Page<RmaDetail> findByProductAndRmaStatus(Product product, Status status, Pageable pageable) {
		return this.rmaDetailRepository.findByProductAndRmaStatus(product, status, pageable);
	}

}
