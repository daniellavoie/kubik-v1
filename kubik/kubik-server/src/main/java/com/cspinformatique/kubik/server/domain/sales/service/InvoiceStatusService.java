package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;

public interface InvoiceStatusService {
	InvoiceStatus findOne(String id);
	
	InvoiceStatus findByType(String type);
}
