package com.cspinformatique.kubik.server.domain.accounting.service;

import com.cspinformatique.kubik.server.domain.accounting.model.CustomerCreditEntries;
import com.cspinformatique.kubik.server.domain.accounting.model.InvoiceEntries;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit;
import com.cspinformatique.kubik.server.model.sales.Invoice;

public interface TransactionEntriesService {
	CustomerCreditEntries generateCustomerCreditEntries(CustomerCredit customerCredit);
	
	InvoiceEntries generateInvoiceEntries(Invoice invoice);
}
