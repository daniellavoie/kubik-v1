package com.cspinformatique.kubik.sales.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.sales.model.InvoiceStatus;
import com.cspinformatique.kubik.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.sales.service.InvoiceStatusService;

@Service
public class InvoiceStatusServiceImpl implements InvoiceStatusService {
	@Autowired private InvoiceStatusRepository invoiceStatusRepository;
	
	@Override
	public InvoiceStatus findOne(String id) {
		return this.invoiceStatusRepository.findOne(id);
	}

}
