package com.cspinformatique.kubik.domain.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.CustomerCreditDetailRepository;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditDetailService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;

@Service
public class CustomerCreditDetailServiceImpl implements
		CustomerCreditDetailService {
	@Autowired
	private CustomerCreditDetailRepository customerCreditDetailRepository;

	@Override
	public Page<CustomerCreditDetail> findByProductAndCustomerCreditStatus(
			Product product, Status status, Pageable pageable) {
		return this.customerCreditDetailRepository
				.findByProductAndInvoiceStatus(product, status, pageable);
	}
}
