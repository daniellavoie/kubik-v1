package com.cspinformatique.kubik.domain.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;

public interface CustomerCreditDetailService {
	Page<CustomerCreditDetail> findByProductAndCustomerCreditStatus(Product product,
			Status status, Pageable pageable);
}
