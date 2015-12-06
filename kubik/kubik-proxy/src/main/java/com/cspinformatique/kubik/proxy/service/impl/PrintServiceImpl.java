package com.cspinformatique.kubik.proxy.service.impl;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;

import javax.print.DocFlavor;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.cspinformatique.kubik.proxy.exception.PrinterNotFoundException;
import com.cspinformatique.kubik.proxy.service.PrintService;
import com.cspinformatique.kubik.proxy.service.ServerService;
import com.cspinformatique.kubik.server.model.print.ReceiptPrintJob;

@Service
public class PrintServiceImpl implements PrintService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintServiceImpl.class);
	
	private static final DocFlavor PRINT_FLAVOR = DocFlavor.INPUT_STREAM.AUTOSENSE;

	@Autowired
	private ServerService serverService;

	private javax.print.PrintService printer;

	@Value("${kubik.proxy.printer.name}")
	private String printerName;
	
	private boolean lostPrinter = false;
	private boolean lostServer = false;

	private void loadPrinterConfiguration(){
		javax.print.PrintService[] services = PrintServiceLookup
				.lookupPrintServices(PRINT_FLAVOR, null);
		this.printer = null;
		
		for (javax.print.PrintService service : services) {
			if (service.getName().contains(printerName)) {
				this.printer = service;
			}
		}
		
		if(this.printer == null){
			throw new PrinterNotFoundException(printerName);
		}
	}

	@Override
	public void executePrintJobs() {
		try{
			for (ReceiptPrintJob receiptPrintJob : this.serverService
					.findPendingReceiptPrintJob()) {
				this.loadPrinterConfiguration();
				
				if(this.lostPrinter){
					this.lostPrinter = false;
					LOGGER.info("Printer has been reconfigured successfully.");
				}
				
				this.print(receiptPrintJob);
			}
			
			if(this.lostServer){
				this.lostServer = false;
				LOGGER.info("Connection with server re established successfully.");
			}
		}catch(ResourceAccessException resourceAccessEx){
			if(!this.lostServer){
				LOGGER.error("Lost connection to server.", resourceAccessEx);
				this.lostServer = true;
			}
		}catch(PrinterNotFoundException printNotFoundEx){
			if(!this.lostPrinter){
				LOGGER.error("Printer " + printNotFoundEx.getPrinterName() + " configuration has been lost.");
				this.lostPrinter = true;
			}
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
