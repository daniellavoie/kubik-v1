package com.cspinformatique.kubik.print.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.model.print.ReceiptPrintJob;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.print.repository.PrintJobRepository;
import com.cspinformatique.kubik.print.service.PrintService;

@Service
public class PrintServiceImpl implements PrintService {
	@Autowired
	private PrintJobRepository printJobRepository;

	private Map<Integer, ReceiptPrintJob> receiptPrintJobs;

	public PrintServiceImpl() {
		this.receiptPrintJobs = new HashMap<Integer, ReceiptPrintJob>();
	}

	@Override
	public List<ReceiptPrintJob> findPendingReceiptPrintJobs() {
		return this.printJobRepository.findAll();
	}

	@Override
	public ReceiptPrintJob createReceiptPrintJob(Invoice invoice) {
		ReceiptPrintJob job = this.printJobRepository.save(new ReceiptPrintJob(
				null, invoice, new Date()));

		this.receiptPrintJobs.put(job.getId(), job);

		return job;
	}

	public void deleteReceiptPrintJob(int id) {
		this.printJobRepository.delete(id);

		this.receiptPrintJobs.remove(id);
	}
}
