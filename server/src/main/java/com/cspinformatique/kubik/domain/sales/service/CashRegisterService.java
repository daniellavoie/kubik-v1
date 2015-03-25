package com.cspinformatique.kubik.domain.sales.service;

import javax.servlet.http.HttpServletRequest;

import com.cspinformatique.kubik.sales.model.CashRegister;

public interface CashRegisterService {
	CashRegister getCashRegister(HttpServletRequest request);
	
	CashRegister save(CashRegister cashRegister);
}
