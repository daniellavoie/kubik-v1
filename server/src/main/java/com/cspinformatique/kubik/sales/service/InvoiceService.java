package com.cspinformatique.kubik.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.sales.model.CashRegisterSession;
import com.cspinformatique.kubik.sales.model.Invoice;
import com.cspinformatique.kubik.sales.model.InvoiceStatus;

public interface InvoiceService {
	Iterable<Invoice> findInvoiceByCashRegisterSessionAndInDraft(CashRegisterSession session);
	
	Invoice generateNewInvoice(CashRegisterSession session);
	
	Page<Invoice> findAll(Pageable pageable);
	
	Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);
	
	Invoice findOne(int id);
	
	Invoice save(Invoice invoice);
}
