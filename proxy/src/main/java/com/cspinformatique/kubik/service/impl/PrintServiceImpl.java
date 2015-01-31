package com.cspinformatique.kubik.service.impl;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;

import javax.print.DocFlavor;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.print.model.ReceiptPrintJob;
import com.cspinformatique.kubik.service.PrintService;
import com.cspinformatique.kubik.service.ServerService;

@Service
public class PrintServiceImpl implements PrintService, InitializingBean {
	private static final DocFlavor PRINT_FLAVOR = DocFlavor.INPUT_STREAM.AUTOSENSE;

	@Autowired
	private ServerService serverService;

	private javax.print.PrintService printer;

	@Value("${kubik.proxy.printer.name}")
	private String printerName;

	@Override
	public void afterPropertiesSet() throws Exception {
		javax.print.PrintService[] services = PrintServiceLookup
				.lookupPrintServices(PRINT_FLAVOR, null);

		for (javax.print.PrintService service : services) {
			if (service.getName().contains(printerName)) {
				printer = service;
				break;
			}
		}

		if (printer == null) {
			throw new RuntimeException("Printer " + printerName
					+ " could not be found.");
		}
	}

	@Override
	public void executePrintJobs() {
		for (ReceiptPrintJob receiptPrintJob : this.serverService
				.findPendingReceiptPrintJob()) {
			this.print(receiptPrintJob);
		}
	}

	private void print(ReceiptPrintJob receiptPrintJob) {
		try {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintService(printer);

			PDDocument.load(
					new ByteArrayInputStream(this.serverService
							.loadInvoiceReceiptData(receiptPrintJob
									.getInvoice()))).silentPrint(job);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			this.serverService.deleteReceiptPrintJob(receiptPrintJob.getId());
		}
	}
}
