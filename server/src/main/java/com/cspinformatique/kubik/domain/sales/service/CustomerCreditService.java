package com.cspinformatique.kubik.domain.sales.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.sales.Customer;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.Invoice;

public interface CustomerCreditService {
	Iterable<CustomerCredit> findAll();
	
	Page<CustomerCredit> findAll(Pageable pageable);
	
	List<CustomerCredit> findByCompleteDate(Date date);
	
	Page<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate, Date endDate, Status status, Pageable pageable);
	
	List<CustomerCredit> findByInvoice(Invoice invoice);
	
	Page<CustomerCredit> findByStatusAndNumberIsNotNull(Status status, Pageable pageable);
	
	Double findCustomerCreditAvailable(Customer customer);
	
	CustomerCredit findOne(int id);
	
	double findProductQuantityReturned(int productId);
	
	Integer findNext(int invoiceId);
	
	Integer findPrevious(int invoiceId);
	
	CustomerCredit save(CustomerCredit customerCredit);
}
