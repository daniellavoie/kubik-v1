package com.cspinformatique.kubik.domain.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.domain.purchase.model.Reception.Status;
import com.cspinformatique.kubik.domain.purchase.repository.ReceptionDetailRepository;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionDetailService;
import com.cspinformatique.kubik.model.product.Product;

@Service
public class ReceptionDetailServiceImpl implements ReceptionDetailService {
	@Autowired private ReceptionDetailRepository receptionDetailRepository;
	
	@Override
	public Page<ReceptionDetail> findByProductAndReceptionStatus(
			Product product, Status status, Pageable pageable) {
		return this.receptionDetailRepository.findByProductAndReceptionStatus(product, status, pageable);
	}
	
	@Override
	public ReceptionDetail save(ReceptionDetail receptionDetail) {
		return this.receptionDetailRepository.save(receptionDetail);
	}

}
