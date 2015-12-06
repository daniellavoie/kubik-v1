package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.sales.repository.CustomerCreditDetailRepository;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerCreditDetailService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit.Status;

@Service
public class CustomerCreditDetailServiceImpl implements
		CustomerCreditDetailService {

	@Autowired
	private CustomerCreditDetailRepository customerCreditDetailRepository;

	@Override
	public List<CustomerCreditDetail> findByProduct(Product product) {
		return this.customerCreditDetailRepository.findByProduct(product);
	}

	@Override
	public Page<CustomerCreditDetail> findByProductAndCustomerCreditStatus(
			Product product, Status status, Pageable pageable) {
		return this.customerCreditDetailRepository
				.findByProductAndInvoiceStatus(product, status, pageable);
	}
	
	@Override
	public CustomerCreditDetail save(CustomerCreditDetail customerCreditDetail){
		return this.customerCreditDetailRepository.save(customerCreditDetail);
	}
}
