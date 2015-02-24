package com.cspinformatique.kubik.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.sales.model.CustomerCredit;

public interface CustomerCreditService {
	Page<CustomerCredit> findAll(Pageable pageable);
	
	CustomerCredit findOne(int id);
	
	CustomerCredit save(CustomerCredit customerCredit);
}
