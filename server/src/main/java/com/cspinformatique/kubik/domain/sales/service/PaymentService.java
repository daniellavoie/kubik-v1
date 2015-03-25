package com.cspinformatique.kubik.domain.sales.service;

import com.cspinformatique.kubik.sales.model.Payment;

public interface PaymentService {
	Iterable<Payment> save(Iterable<Payment> payments);
}
