package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.server.model.sales.PaymentMethod;

public interface PaymentMethodService {
	Iterable<PaymentMethod> findAll();
	
	PaymentMethod findOne(String id);
	
	PaymentMethod findByType(String type);
}
