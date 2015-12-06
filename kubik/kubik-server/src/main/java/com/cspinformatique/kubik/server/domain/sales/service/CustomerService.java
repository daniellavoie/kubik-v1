package com.cspinformatique.kubik.server.domain.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.server.model.sales.Customer;

public interface CustomerService {

	Iterable<Customer> findAll();

	Page<Customer> findAll(Pageable pageable);

	Customer findOne(int id);

	String getCustomerAccount(Customer customer);

	void processNotification(KubikNotification kubikNotification);

	Customer save(Customer customer);

	Page<Customer> search(String query, Pageable pageable);
}
