package com.cspinformatique.kubik.server.domain.purchase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.purchase.Rma;

public interface RmaService {
	Page<Rma> findAll(Pageable pageable);
	
	Rma findOne(int id);
	
	Integer findNext(int rmaId);
	
	Integer findPrevious(int rmaId);
	
	double findProductQuantityReturnedToSupplier(int productId);
	
	Rma save(Rma rma);
}
