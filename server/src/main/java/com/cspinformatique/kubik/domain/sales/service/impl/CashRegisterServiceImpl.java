package com.cspinformatique.kubik.domain.sales.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.CashRegisterRepository;
import com.cspinformatique.kubik.domain.sales.service.CashRegisterService;


import com.cspinformatique.kubik.sales.model.CashRegister;

@Service
public class CashRegisterServiceImpl implements CashRegisterService {
	@Autowired private CashRegisterRepository cashRegisterRepository;
	
	@Override
	public CashRegister getCashRegister(HttpServletRequest request){
		CashRegister cashRegister = this.cashRegisterRepository.findOne(request.getRemoteHost()); 

		if(cashRegister != null){
			return cashRegister;
		}
		
		return this.cashRegisterRepository.save(new CashRegister(request.getRemoteHost(), request.getRemoteAddr()));
	}

	@Override
	public CashRegister save(CashRegister cashRegister) {
		return this.cashRegisterRepository.save(cashRegister);
	}

}
