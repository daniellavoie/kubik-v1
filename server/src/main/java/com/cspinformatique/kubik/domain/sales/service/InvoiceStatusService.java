package com.cspinformatique.kubik.domain.sales.service;

import com.cspinformatique.kubik.sales.model.InvoiceStatus;

public interface InvoiceStatusService {
	InvoiceStatus findOne(String id);
}
