package com.cspinformatique.kubik.domain.sales.service;

import com.cspinformatique.kubik.model.sales.InvoiceStatus;

public interface InvoiceStatusService {
	InvoiceStatus findOne(String id);
}
