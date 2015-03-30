package com.cspinformatique.kubik.domain.sales.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.CashRegisterSessionRepository;
import com.cspinformatique.kubik.domain.sales.service.CashRegisterSessionService;
import com.cspinformatique.kubik.model.sales.CashRegister;
import com.cspinformatique.kubik.model.sales.CashRegisterSession;

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
