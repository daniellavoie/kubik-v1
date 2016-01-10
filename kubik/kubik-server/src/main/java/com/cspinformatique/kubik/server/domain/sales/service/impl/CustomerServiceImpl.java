package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.kubik.KubikNotification;
import com.cspinformatique.kubik.server.domain.kos.rest.KosTemplate;
import com.cspinformatique.kubik.server.domain.sales.repository.CustomerRepository;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.server.model.sales.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final String CUSTOMER_ACCOUNT_PREFIX = "411";

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private KosTemplate kosTemplate;

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
	public void processNotification(KubikNotification kubikNotification) {
		Account account = kosTemplate.exchange("/account/" + kubikNotification.getKosId(), HttpMethod.GET,
				Account.class);

		Customer customer = customerRepository.findByEmail(account.getUsername());

		if (customer == null) {
			customer = new Customer();
			customer.setEmail(account.getUsername());
		}

		if (account.getBillingAddress() != null) {
			customer.setFirstName(account.getBillingAddress().getFirstName());
			customer.setLastName(account.getBillingAddress().getLastName());
		}

		save(customer);
	}

	@Override
	public Customer save(Customer customer) {
		if (customer.getCreationDate() == null) {
			customer.setCreationDate(new Date());
		}

		return this.customerRepository.save(customer);
	}

	@Override
	public Page<Customer> search(String query, Pageable pageable) {
		return this.customerRepository.search("%" + query + "%", pageable);
	}

}
