package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.sales.model.CashRegister;

public interface CashRegisterRepository extends
		PagingAndSortingRepository<CashRegister, String> {

}
