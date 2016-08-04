package com.cspinformatique.kubik.proxy.service;

import com.cspinformatique.kubik.proxy.model.ReceiptPrintJob;

public interface ServerService {
	void deleteReceiptPrintJob(int id);

	Iterable<ReceiptPrintJob> findPendingReceiptPrintJob();

	byte[] loadInvoiceReceiptData(int invoiceId);
}
