package com.cspinformatique.kubik.server.domain.purchase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.purchase.repository.RmaDetailRepository;
import com.cspinformatique.kubik.server.domain.purchase.service.RmaDetailService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.RmaDetail;
import com.cspinformatique.kubik.server.model.purchase.Rma.Status;

@Service
public class RmaDetailServiceImpl implements RmaDetailService {
	@Autowired
	private RmaDetailRepository rmaDetailRepository;
	
	@Override
	public List<RmaDetail> findByProduct(Product product){
		return this.rmaDetailRepository.findByProduct(product);
	}
	
	@Override
	public Page<RmaDetail> findByProductAndRmaStatus(Product product, Status status, Pageable pageable) {
		return this.rmaDetailRepository.findByProductAndRmaStatus(product, status, pageable);
	}
	
	@Override
	public RmaDetail save(RmaDetail rmaDetail){
		return this.rmaDetailRepository.save(rmaDetail);
	}

}
