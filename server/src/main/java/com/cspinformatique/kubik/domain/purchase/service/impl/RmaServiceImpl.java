package com.cspinformatique.kubik.domain.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.model.Rma;
import com.cspinformatique.kubik.domain.purchase.repository.RmaRepository;
import com.cspinformatique.kubik.domain.purchase.service.RmaService;

@Service
public class RmaServiceImpl implements RmaService {
	@Autowired
	private RmaRepository rmaRepository;

	@Override
	public Page<Rma> findAll(Pageable pageable) {
		return this.rmaRepository.findAll(pageable);
	}
	
	@Override
	public Rma findOne(int id){
		return this.rmaRepository.findOne(id);
	}

	@Override
	public Rma save(Rma rma) {
		return this.rmaRepository.save(rma);
	}

}
