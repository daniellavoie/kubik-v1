package com.cspinformatique.kubik.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.sales.model.CustomerCredit;

public interface CustomerCreditRepository extends
		JpaRepository<CustomerCredit, Integer> {

}
