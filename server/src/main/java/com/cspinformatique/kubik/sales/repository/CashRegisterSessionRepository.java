package com.cspinformatique.kubik.sales.repository;

import java.util.Date;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.sales.model.CashRegister;
import com.cspinformatique.kubik.sales.model.CashRegisterSession;

public interface CashRegisterSessionRepository extends
		PagingAndSortingRepository<CashRegisterSession, Integer> {
	public CashRegisterSession findByCashRegisterAndEndDate(CashRegister cashRegister, Date endDate);
}
