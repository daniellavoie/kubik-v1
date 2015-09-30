package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.repository.ReceptionDetailRepository;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionDetailService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.Reception.Status;
import com.cspinformatique.kubik.model.purchase.ReceptionDetail;

@Service
public class ReceptionDetailServiceImpl implements ReceptionDetailService {
	@Autowired
	private ReceptionDetailRepository receptionDetailRepository;

	@Override
	public List<ReceptionDetail> findByProduct(Product product) {
		return receptionDetailRepository.findByProduct(product);
	}

	@Override
	public Page<ReceptionDetail> findByProductAndReceptionStatus(Product product, Status status, Pageable pageable) {
		return receptionDetailRepository.findByProductAndReceptionStatus(product, status, pageable);
	}

	@Override
	public ReceptionDetail save(ReceptionDetail receptionDetail) {
		return receptionDetailRepository.save(receptionDetail);
	}
}
