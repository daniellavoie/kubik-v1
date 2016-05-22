package com.cspinformatique.kubik.server.domain.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;

@Service
public class InvoiceStatusServiceImpl implements InvoiceStatusService {
	@Autowired
	private InvoiceStatusRepository invoiceStatusRepository;

	@Override
	public InvoiceStatus findOne(String id) {
		return this.invoiceStatusRepository.findOne(id);
	}

	@Override
	public InvoiceStatus findByType(String type) {
		return invoiceStatusRepository.findByType(type);
	}
	
	@Override
	public InvoiceStatus save(InvoiceStatus invoiceStatus) {
		return invoiceStatusRepository.save(invoiceStatus);
	}
}
