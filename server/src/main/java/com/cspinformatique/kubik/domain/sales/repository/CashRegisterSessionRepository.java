package com.cspinformatique.kubik.domain.sales.repository;

import java.util.Date;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.sales.CashRegister;
import com.cspinformatique.kubik.model.sales.CashRegisterSession;

public interface CashRegisterSessionRepository extends
		PagingAndSortingRepository<CashRegisterSession, Integer> {
	CashRegisterSession findByCashRegisterAndEndDate(CashRegister cashRegister, Date endDate);
}
