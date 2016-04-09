package com.cspinformatique.kubik.server.domain.sales.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.sales.CashRegisterSession;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;

public interface InvoiceRepository
		extends PagingAndSortingRepository<Invoice, Integer>, QueryDslPredicateExecutor<Invoice> {
	List<Invoice> findByCashRegisterSessionAndStatus(CashRegisterSession session, InvoiceStatus invoiceStatus);

	Invoice findByNumber(String number);

	List<Invoice> findByPaidDateBetweenAndStatus(Date startPaidDate, Date startEndDate, InvoiceStatus status);

	Page<Invoice> findByPaidDateBetweenAndStatus(Date startPaidDate, Date startEndDate, InvoiceStatus status,
			Pageable pageable);

	Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

	Page<Invoice> findByStatusAndNumberIsNotNull(InvoiceStatus status, Pageable pageable);

	List<Invoice> findByStatusAndPaidDateAfter(InvoiceStatus status, Date paidDate);

	@Query("SELECT invoiceDetail FROM Invoice invoice, InvoiceDetail invoiceDetail where invoice = invoiceDetail.invoice AND invoice.id = ?1 AND invoiceDetail.product.ean13 = ?2")
	InvoiceDetail findDetailByInvoiceIdAndProductEan13(int invoiceId, String ean13);

	@Query("SELECT id FROM Invoice invoice WHERE id > ?1")
	Page<Integer> findIdByIdGreaterThan(int id, Pageable pageable);

	@Query("SELECT id FROM Invoice invoice WHERE id < ?1")
	Page<Integer> findIdByIdLessThan(int id, Pageable pageable);

	@Query("SELECT sum(detail.quantity) FROM InvoiceDetail detail WHERE detail.product.id = ?1 AND detail.invoice.status.type = 'ORDER_CONFIRMED'")
	Double findProductQuantityOnHold(int productId);

	@Query("SELECT sum(detail.quantity) FROM InvoiceDetail detail WHERE detail.product.id = ?1 AND detail.invoice.status.type = 'ORDER_CONFIRMED' AND detail.invoice.confirmedDate < ?2")
	Double findProductQuantityOnHoldUntil(int productId, Date until);

	@Query("SELECT sum(detail.quantity) FROM InvoiceDetail detail WHERE detail.product.id = ?1 AND detail.invoice.status.type = 'PAID'")
	Double findProductQuantitySold(int productId);

	@Query("SELECT sum(detail.quantity) FROM InvoiceDetail detail WHERE detail.product.id = ?1 AND detail.invoice.status.type = 'PAID' AND detail.invoice.paidDate < ?2")
	Double findProductQuantitySoldUntil(int productId, Date until);
}
