package com.cspinformatique.kubik.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.sales.model.PaymentMethod;
import com.cspinformatique.kubik.sales.repository.PaymentMethodRepository;
import com.cspinformatique.kubik.sales.service.PaymentMethodService;

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
