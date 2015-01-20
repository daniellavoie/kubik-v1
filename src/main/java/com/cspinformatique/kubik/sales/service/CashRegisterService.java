package com.cspinformatique.kubik.sales.service;

import javax.servlet.http.HttpServletRequest;

import com.cspinformatique.kubik.sales.model.CashRegister;

public interface CashRegisterService {
	public CashRegister getCashRegister(HttpServletRequest request);
	
	public CashRegister save(CashRegister cashRegister);
}
