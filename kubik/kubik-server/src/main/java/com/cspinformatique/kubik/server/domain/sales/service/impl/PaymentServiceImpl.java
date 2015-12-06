package com.cspinformatique.kubik.server.domain.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.sales.repository.PaymentRepository;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentService;
import com.cspinformatique.kubik.server.model.sales.Payment;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired private PaymentRepository paymentRepository;
	
	@Override
	public Iterable<Payment> save(Iterable<Payment> payments) {
		return this.paymentRepository.save(payments);
	}
}
