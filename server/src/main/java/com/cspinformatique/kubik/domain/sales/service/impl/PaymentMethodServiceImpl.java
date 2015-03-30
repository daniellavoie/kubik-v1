package com.cspinformatique.kubik.domain.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.PaymentMethodRepository;
import com.cspinformatique.kubik.domain.sales.service.PaymentMethodService;
import com.cspinformatique.kubik.model.sales.PaymentMethod;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
	@Autowired private PaymentMethodRepository paymentMethodRepository;
	
	@Override
	public Iterable<PaymentMethod> findAll(){
		return this.paymentMethodRepository.findAll();
	}
	
	@Override
	public PaymentMethod findOne(String id) {
		return this.paymentMethodRepository.findOne(id);
	}

}
