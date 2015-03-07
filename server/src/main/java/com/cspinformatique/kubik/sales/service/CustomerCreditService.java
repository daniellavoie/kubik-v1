package com.cspinformatique.kubik.sales.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.sales.model.Customer;
import com.cspinformatique.kubik.sales.model.CustomerCredit;
import com.cspinformatique.kubik.sales.model.Invoice;

public interface CustomerCreditService {
	Page<CustomerCredit> findAll(Pageable pageable);
	
	List<CustomerCredit> findByCompleteDate(Date date);
	
	List<CustomerCredit> findByInvoice(Invoice invoice);
	
	Double findCustomerCreditAvailable(Customer customer);
	
	CustomerCredit findOne(int id);
	
	Integer findNext(int invoiceId);
	
	Integer findPrevious(int invoiceId);
	
	CustomerCredit save(CustomerCredit customerCredit);
}
