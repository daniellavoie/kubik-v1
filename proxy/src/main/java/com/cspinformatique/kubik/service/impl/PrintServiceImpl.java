package com.cspinformatique.kubik.service.impl;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;

import javax.print.DocFlavor;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.service.PrintService;

@Service
public class PrintServiceImpl implements PrintService, InitializingBean {
	private static final DocFlavor PRINT_FLAVOR = DocFlavor.INPUT_STREAM.AUTOSENSE;
	
	private javax.print.PrintService printer;

	@Value("${kubik.proxy.printer.name}")
	private String printerName;
	
	@Override
	public void afterPropertiesSet() throws Exception {
	    javax.print.PrintService[] services = PrintServiceLookup.lookupPrintServices(PRINT_FLAVOR, null);
	    
        for(javax.print.PrintService service : services) {
            if(service.getName().contains(printerName)) {
             	printer = service;
                break;
            }
        }
        
        if(printer == null){
        	throw new RuntimeException("Printer " + printerName + " could not be found.");
        }
	}
	
	@Override
	public void print(byte[] content) {
		try {
			PrinterJob job = PrinterJob.getPrinterJob();
		    job.setPrintService(printer);
		    
		    PDDocument.load(new ByteArrayInputStream(content)).silentPrint(job);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
