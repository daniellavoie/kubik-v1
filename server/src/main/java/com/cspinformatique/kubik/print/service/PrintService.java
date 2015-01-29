package com.cspinformatique.kubik.print.service;

import java.util.List;

import com.cspinformatique.kubik.print.model.ReceiptPrintJob;
import com.cspinformatique.kubik.sales.model.Invoice;

public interface PrintService {
	List<ReceiptPrintJob> findPendingReceiptPrintJobs();
	
	ReceiptPrintJob createReceiptPrintJob(Invoice invoice);
	
	void deleteReceiptPrintJob(int id);
}
