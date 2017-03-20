package com.cspinformatique.kubik.server.domain.sales.service;

import java.util.List;

import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceConfirmation;
import com.cspinformatique.kubik.server.model.sales.InvoiceConfirmation.Status;

public interface InvoiceConfirmationService {

	void create(Invoice invoice);
	
	List<InvoiceConfirmation> findByStatus(Status status);

	void process(InvoiceConfirmation invoiceConfirmation, CustomerOrder customerOrder);
}
