package com.cspinformatique.kubik.sales.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.sales.model.CashRegister;

public interface CashRegisterRepository extends
		PagingAndSortingRepository<CashRegister, String> {

}
