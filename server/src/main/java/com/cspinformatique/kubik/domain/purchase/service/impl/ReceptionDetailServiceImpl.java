package com.cspinformatique.kubik.domain.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.domain.purchase.repository.ReceptionDetailRepository;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionDetailService;

@Service
public class ReceptionDetailServiceImpl implements ReceptionDetailService {
	@Autowired private ReceptionDetailRepository receptionDetailRepository;
	
	@Override
	public ReceptionDetail save(ReceptionDetail receptionDetail) {
		return this.receptionDetailRepository.save(receptionDetail);
	}

}
