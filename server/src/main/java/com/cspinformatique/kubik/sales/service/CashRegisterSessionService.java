package com.cspinformatique.kubik.sales.service;

import com.cspinformatique.kubik.sales.model.CashRegister;
import com.cspinformatique.kubik.sales.model.CashRegisterSession;

public interface CashRegisterSessionService {	
	CashRegisterSession closeSession(int id);
	
	CashRegisterSession findByCashRegister(CashRegister cashRegister);
	
	CashRegisterSession findOne(int id);
	
	CashRegisterSession save(CashRegisterSession cashRegisterSession);

}
