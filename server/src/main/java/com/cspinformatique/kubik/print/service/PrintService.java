package com.cspinformatique.kubik.print.service;

import java.util.List;

import com.cspinformatique.kubik.model.print.ReceiptPrintJob;
import com.cspinformatique.kubik.model.sales.Invoice;

public interface PrintService {
	List<ReceiptPrintJob> findPendingReceiptPrintJobs();
	
	ReceiptPrintJob createReceiptPrintJob(Invoice invoice);
	
	void deleteReceiptPrintJob(int id);
}
