package com.cspinformatique.kubik.server.domain.sales.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.sales.CashRegister;

public interface CashRegisterRepository extends
		PagingAndSortingRepository<CashRegister, String> {

}
