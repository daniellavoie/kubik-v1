package com.cspinformatique.kubik.domain.sales.service;

import com.cspinformatique.kubik.sales.model.PaymentMethod;

public interface PaymentMethodService {
	Iterable<PaymentMethod> findAll();
	
	PaymentMethod findOne(String id);
}
