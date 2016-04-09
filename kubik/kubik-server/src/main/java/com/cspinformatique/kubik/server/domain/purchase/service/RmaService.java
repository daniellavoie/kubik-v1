package com.cspinformatique.kubik.server.domain.purchase.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.purchase.Rma.Status;

public interface RmaService {
	Page<Rma> findAll(Pageable pageable);
	
	List<Rma> findByStatusAndShippedDateAfter(Status status, Date shippedDate);
	
	Rma findOne(int id);
	
	Integer findNext(int rmaId);
	
	Integer findPrevious(int rmaId);
	
	double findProductQuantityReturnedToSupplier(int productId);
	
	Rma save(Rma rma);
}
