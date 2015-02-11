package com.cspinformatique.kubik.purchase.service;

import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.purchase.model.Reception;

public interface ReceptionService {
	public Iterable<Reception> findAll(Pageable pageable);
	
	public Reception findOne(int id);

	public Reception save(Reception reception);
}
