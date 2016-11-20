package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.server.model.sales.Invoice;

public interface InvoiceConfirmationService {

	void create(Invoice invoice);
	
	void processConfirmations();
	
	void recoverConfirmations();
}
