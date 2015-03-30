package com.cspinformatique.kubik.domain.sales.service;

import com.cspinformatique.kubik.model.sales.Payment;

public interface PaymentService {
	Iterable<Payment> save(Iterable<Payment> payments);
}
