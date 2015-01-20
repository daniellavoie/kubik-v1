package com.cspinformatique.kubik.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.sales.model.Payment;
import com.cspinformatique.kubik.sales.repository.PaymentRepository;
import com.cspinformatique.kubik.sales.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired private PaymentRepository paymentRepository;
	
	@Override
	public Iterable<Payment> save(Iterable<Payment> payments) {
		return this.paymentRepository.save(payments);
	}
}
