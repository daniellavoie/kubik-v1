package com.cspinformatique.kubik.server.domain.sales.exception;

import com.cspinformatique.kubik.server.model.sales.Invoice;

public class InvoiceAlreadyPaidException extends RuntimeException {
	private static final long serialVersionUID = 1669479223189083696L;

	private final Invoice existingInvoice;
	private final Invoice newInvoice;

	public InvoiceAlreadyPaidException(Invoice existingInvoice, Invoice newInvoice) {
		super("Invoice " + newInvoice.getId() + " has already been paid.");
		
		this.existingInvoice = existingInvoice;
		this.newInvoice = newInvoice;
	}

	public Invoice getExistingInvoice() {
		return existingInvoice;
	}

	public Invoice getNewInvoice() {
		return newInvoice;
	}
}
