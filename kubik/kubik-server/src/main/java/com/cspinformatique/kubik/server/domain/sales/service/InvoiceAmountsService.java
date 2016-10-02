package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;

public interface InvoiceAmountsService {
	void calculateDetailAmounts(InvoiceDetail invoiceDetail);
}
