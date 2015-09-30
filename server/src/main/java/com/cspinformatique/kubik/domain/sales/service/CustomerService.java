package com.cspinformatique.kubik.domain.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.sales.Customer;

public interface CustomerService {

	Iterable<Customer> findAll();

	Page<Customer> findAll(Pageable pageable);

	Customer findOne(int id);

	String getCustomerAccount(Customer customer);

	Customer save(Customer customer);

	Page<Customer> search(String query, Pageable pageable);
}
