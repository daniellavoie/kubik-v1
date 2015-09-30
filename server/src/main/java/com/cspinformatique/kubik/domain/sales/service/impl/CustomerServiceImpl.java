package com.cspinformatique.kubik.domain.sales.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.CustomerRepository;
import com.cspinformatique.kubik.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.model.sales.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final String CUSTOMER_ACCOUNT_PREFIX = "411";
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Iterable<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	@Override
	public Page<Customer> findAll(Pageable pageable) {
		return this.customerRepository.findAll(pageable);
	}

	@Override
	public Customer findOne(int id) {
		return this.customerRepository.findOne(id);
	}

	@Override
	public String getCustomerAccount(Customer customer) {
		String customerAccount = CUSTOMER_ACCOUNT_PREFIX + "99999";

		if (customer != null) {
			customerAccount = CUSTOMER_ACCOUNT_PREFIX + String.format("%05d", customer.getId());
		}

		return customerAccount;
	}

	@Override
	public Customer save(Customer customer) {
		if(customer.getCreationDate() == null){
			customer.setCreationDate(new Date());
		}
		
		return this.customerRepository.save(customer);
	}

	@Override
	public Page<Customer> search(String query, Pageable pageable) {
		return this.customerRepository.search("%" + query  + "%", pageable);
	}

}
