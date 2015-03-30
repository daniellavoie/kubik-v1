package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.model.sales.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
	
}
