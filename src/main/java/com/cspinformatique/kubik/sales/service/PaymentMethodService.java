package com.cspinformatique.kubik.sales.service;

import com.cspinformatique.kubik.sales.model.PaymentMethod;


public interface PaymentMethodService {
	public Iterable<PaymentMethod> findAll();
	
	public PaymentMethod findOne(String id);
}
