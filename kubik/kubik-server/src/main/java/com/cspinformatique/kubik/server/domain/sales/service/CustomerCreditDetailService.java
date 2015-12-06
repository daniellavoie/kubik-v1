package com.cspinformatique.kubik.server.domain.sales.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit.Status;

public interface CustomerCreditDetailService {
	List<CustomerCreditDetail> findByProduct(Product product);
	
	Page<CustomerCreditDetail> findByProductAndCustomerCreditStatus(Product product,
			Status status, Pageable pageable);
	
	CustomerCreditDetail save(CustomerCreditDetail customerCreditDetail);
}
