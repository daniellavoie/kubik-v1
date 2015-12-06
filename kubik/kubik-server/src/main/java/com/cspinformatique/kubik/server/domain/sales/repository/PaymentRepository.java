package com.cspinformatique.kubik.server.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.server.model.sales.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
	
}
