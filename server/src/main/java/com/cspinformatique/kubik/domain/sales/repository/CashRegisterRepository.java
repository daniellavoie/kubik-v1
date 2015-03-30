package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.sales.CashRegister;

public interface CashRegisterRepository extends
		PagingAndSortingRepository<CashRegister, String> {

}
