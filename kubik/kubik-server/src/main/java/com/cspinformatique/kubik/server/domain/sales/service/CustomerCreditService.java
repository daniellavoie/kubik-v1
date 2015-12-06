package com.cspinformatique.kubik.server.domain.sales.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.sales.Customer;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit.Status;

public interface CustomerCreditService {
	Iterable<CustomerCredit> findAll();
	
	Page<CustomerCredit> findAll(Pageable pageable);
	
	List<CustomerCredit> findByCompleteDate(Date date);
	
	Page<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate, Date endDate, Status status, Pageable pageable);
	
	List<CustomerCredit> findByInvoice(Invoice invoice);
	
	Page<CustomerCredit> findByStatusAndNumberIsNotNull(Status status, Pageable pageable);
	
	Double findCustomerCreditAvailable(Customer customer);
	
	CustomerCredit findOne(int id);
	
	double findProductQuantityReturnedByCustomer(int productId);
	
	Integer findNext(int customerCreditId);
	
	Integer findPrevious(int customerCreditId);
	
	void recalculateCustomerCreditsTaxes();
	
	CustomerCredit save(CustomerCredit customerCredit);
}
