package com.cspinformatique.kubik.proxy.service;

import com.cspinformatique.kubik.model.print.ReceiptPrintJob;
import com.cspinformatique.kubik.model.sales.Invoice;

public interface ServerService {
	void deleteReceiptPrintJob(int id);
	
	Iterable<ReceiptPrintJob> findPendingReceiptPrintJob();
	
	byte[] loadInvoiceReceiptData(Invoice invoice);
}
