package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.server.model.sales.Payment;

public interface PaymentService {
	Iterable<Payment> save(Iterable<Payment> payments);
}
