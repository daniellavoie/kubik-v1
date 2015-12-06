package com.cspinformatique.kubik.server.domain.sales.service;

import javax.servlet.http.HttpServletRequest;

import com.cspinformatique.kubik.server.model.sales.CashRegister;

public interface CashRegisterService {
	CashRegister getCashRegister(HttpServletRequest request);
	
	CashRegister save(CashRegister cashRegister);
}
