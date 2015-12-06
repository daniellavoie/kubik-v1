package com.cspinformatique.kubik.server.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.server.model.sales.PaymentMethod;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, String>{

}
