package com.cspinformatique.kubik.domain.accounting.service;

import com.cspinformatique.kubik.domain.accounting.model.CustomerCreditEntries;
import com.cspinformatique.kubik.domain.accounting.model.InvoiceEntries;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.Invoice;

public interface TransactionEntriesService {
	CustomerCreditEntries generateCustomerCreditEntries(CustomerCredit customerCredit);
	
	InvoiceEntries generateInvoiceEntries(Invoice invoice);
}
