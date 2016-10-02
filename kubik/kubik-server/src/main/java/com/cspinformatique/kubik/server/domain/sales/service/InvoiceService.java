package com.cspinformatique.kubik.server.domain.sales.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.sales.CashRegisterSession;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.Invoice.ShippingMethod;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.querydsl.core.types.Predicate;

public interface InvoiceService {
	void calculateInvoiceTaxes(Invoice invoice);

	List<Invoice> findByStatusAndPaidDateAfter(InvoiceStatus status, Date paidDate);

	InvoiceDetail findDetailByInvoiceIdAndProductEan13(int invoiceId, String ean13);

	Iterable<Invoice> findInvoiceByCashRegisterSessionAndInDraft(CashRegisterSession session);

	Invoice generateNewInvoice(Long customerOrderId, Double shippingCost, ShippingMethod shippingMethod);

	Invoice generateNewInvoice(CashRegisterSession session);

	Invoice generateNewOrder(int customerId);

	Page<Invoice> findAll(Pageable pageable);

	Page<Invoice> findAll(Predicate predicate, Pageable pageable);

	Invoice findByNumber(String number);

	List<Invoice> findByPaidDate(Date paidDate);

	Page<Invoice> findByPaidDateBetweenAndStatus(Date startPaidDate, Date startEndDate, InvoiceStatus status,
			Pageable pageable);

	Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

	Invoice findFirstPaidInvoice();

	Integer findNext(int id);

	Invoice findOne(int id);

	Integer findPrevious(int id);

	double findProductQuantityOnHold(int productId);

	double findProductQuantityOnHoldUntil(int productId, Date until);

	double findProductQuantitySold(int productId);

	double findProductQuantitySoldUntil(int productId, Date until);

	void initializeInvoiceNumbers();

	void recalculateDetailsAmounts();

	void recalculateInvoiceTaxes();

	Invoice save(Invoice invoice);
}
