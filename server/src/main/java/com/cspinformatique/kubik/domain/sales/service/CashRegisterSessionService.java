package com.cspinformatique.kubik.domain.sales.service;

import com.cspinformatique.kubik.model.sales.CashRegister;
import com.cspinformatique.kubik.model.sales.CashRegisterSession;

public interface CashRegisterSessionService {	
	CashRegisterSession closeSession(int id);
	
	CashRegisterSession findByCashRegister(CashRegister cashRegister);
	
	CashRegisterSession findOne(int id);
	
	CashRegisterSession save(CashRegisterSession cashRegisterSession);

}
