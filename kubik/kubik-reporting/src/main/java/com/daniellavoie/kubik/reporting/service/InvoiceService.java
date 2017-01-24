package com.daniellavoie.kubik.reporting.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.sales.Invoice;

public interface InvoiceService {
	List<Invoice> findByStatus(String status);
}
