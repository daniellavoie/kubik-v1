package com.cspinformatique.kubik.server.domain.sales.repository;

import java.util.Date;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.sales.CashRegister;
import com.cspinformatique.kubik.server.model.sales.CashRegisterSession;

public interface CashRegisterSessionRepository extends
		PagingAndSortingRepository<CashRegisterSession, Integer> {
	CashRegisterSession findByCashRegisterAndEndDate(CashRegister cashRegister, Date endDate);
}
