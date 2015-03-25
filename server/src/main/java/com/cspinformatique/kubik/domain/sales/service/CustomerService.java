package com.cspinformatique.kubik.domain.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.sales.model.Customer;

public interface CustomerService {
	Iterable<Customer> findAll();
	
	Page<Customer> findAll(Pageable pageable);
	
	Customer findOne(int id);
	
	Customer save(Customer customer);
	
	Page<Customer> search(String query, Pageable pageable);
}
