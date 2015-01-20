package com.cspinformatique.kubik.purchase.service;

import com.cspinformatique.kubik.purchase.model.Reception;

public interface ReceptionService {
	public Iterable<Reception> findAll();
	
	public Reception findOne(int id);

	public Reception save(Reception reception);
}
