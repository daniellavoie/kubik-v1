package com.cspinformatique.kubik.server.print.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.print.ReceiptPrintJob;
import com.cspinformatique.kubik.server.model.sales.Invoice;

public interface PrintService {
	List<ReceiptPrintJob> findPendingReceiptPrintJobs();
	
	ReceiptPrintJob createReceiptPrintJob(Invoice invoice);
	
	void deleteReceiptPrintJob(int id);
}
