package com.cspinformatique.kubik.domain.sales.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.sales.model.CashRegisterSession;
import com.cspinformatique.kubik.sales.model.Invoice;
import com.cspinformatique.kubik.sales.model.InvoiceDetail;
import com.cspinformatique.kubik.sales.model.InvoiceStatus;

public interface InvoiceService {
	InvoiceDetail findDetailByInvoiceIdAndProductEan13(int invoiceId, String ean13);
	
	Iterable<Invoice> findInvoiceByCashRegisterSessionAndInDraft(CashRegisterSession session);
	
	Invoice generateNewInvoice(CashRegisterSession session);
	
	Page<Invoice> findAll(Pageable pageable);
	
	Invoice findByNumber(String number);
	
	List<Invoice> findByPaidDate(Date paidDate);
	
	Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);
	
	Invoice findFirstPaidInvoice();
	
	Integer findNext(int id);
	
	Invoice findOne(int id);
	
	Integer findPrevious(int id);
	
	Double findProductQuantitySold(int productId);
	
	void initializeInvoiceNumbers();
	
	Invoice save(Invoice invoice);
}
