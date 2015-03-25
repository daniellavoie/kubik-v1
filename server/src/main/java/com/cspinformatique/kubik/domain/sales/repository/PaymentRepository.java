package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.sales.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
	
}
