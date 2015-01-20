package com.cspinformatique.kubik.sales.service;

import com.cspinformatique.kubik.sales.model.Payment;

public interface PaymentService {
	public Iterable<Payment> save(Iterable<Payment> payments);
}
