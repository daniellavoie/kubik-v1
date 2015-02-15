package com.cspinformatique.kubik.sales.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.sales.model.CashRegisterSession;
import com.cspinformatique.kubik.sales.model.Invoice;
import com.cspinformatique.kubik.sales.model.InvoiceStatus;

public interface InvoiceRepository extends
		PagingAndSortingRepository<Invoice, Integer> {
	List<Invoice> findByCashRegisterSessionAndStatus(
			CashRegisterSession session, InvoiceStatus invoiceStatus);

	List<Invoice> findByPaidDateBetweenAndStatus(Date startPaidDate,
			Date startEndDate, InvoiceStatus status);

	Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

	@Query("SELECT id FROM Invoice invoice WHERE id > ?")
	Page<Integer> findIdByIdGreaterThan(int id, Pageable pageable);

	@Query("SELECT id FROM Invoice invoice WHERE id < ?")
	Page<Integer> findIdByIdLessThan(int id, Pageable pageable);
}
