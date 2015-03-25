package com.cspinformatique.kubik.domain.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.sales.model.InvoiceStatus;

@Service
public class InvoiceStatusServiceImpl implements InvoiceStatusService {
	@Autowired private InvoiceStatusRepository invoiceStatusRepository;
	
	@Override
	public InvoiceStatus findOne(String id) {
		return this.invoiceStatusRepository.findOne(id);
	}

}
