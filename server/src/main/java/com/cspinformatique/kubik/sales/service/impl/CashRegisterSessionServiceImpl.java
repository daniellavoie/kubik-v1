package com.cspinformatique.kubik.sales.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.sales.model.CashRegister;
import com.cspinformatique.kubik.sales.model.CashRegisterSession;
import com.cspinformatique.kubik.sales.repository.CashRegisterSessionRepository;
import com.cspinformatique.kubik.sales.service.CashRegisterSessionService;

@Service
public class CashRegisterSessionServiceImpl implements
		CashRegisterSessionService {
	@Autowired
	private CashRegisterSessionRepository cashRegisterSessionRepostory;

	@Override
	public CashRegisterSession closeSession(int id) {
		CashRegisterSession session = this.cashRegisterSessionRepostory
				.findOne(id);

		session.setEndDate(new Date());

		return this.save(session);
	}

	@Override
	public CashRegisterSession findByCashRegister(CashRegister cashRegister) {
		// Lookup for an active session.
		CashRegisterSession session = this.cashRegisterSessionRepostory
				.findByCashRegisterAndEndDate(cashRegister, null);

		if (session == null) {
			session = this.save(new CashRegisterSession(null, cashRegister,
					new Date(), null));
		}

		return session;
	}

	@Override
	public CashRegisterSession findOne(int id) {
		return this.cashRegisterSessionRepostory.findOne(id);
	}

	@Override
	public CashRegisterSession save(CashRegisterSession cashRegisterSession) {
		return this.cashRegisterSessionRepostory.save(cashRegisterSession);
	}

}
