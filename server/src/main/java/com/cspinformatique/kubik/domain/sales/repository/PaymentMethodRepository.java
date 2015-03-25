package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.sales.model.PaymentMethod;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, String>{

}
