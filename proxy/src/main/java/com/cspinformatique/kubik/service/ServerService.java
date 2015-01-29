package com.cspinformatique.kubik.service;

import com.cspinformatique.kubik.print.model.ReceiptPrintJob;
import com.cspinformatique.kubik.sales.model.Invoice;

public interface ServerService {
	void deleteReceiptPrintJob(int id);
	
	Iterable<ReceiptPrintJob> findPendingReceiptPrintJob();
	
	byte[] loadInvoiceReceiptData(Invoice invoice);
	
	void registerProxyToServer();
}
