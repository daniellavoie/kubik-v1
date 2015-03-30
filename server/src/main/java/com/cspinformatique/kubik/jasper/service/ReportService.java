package com.cspinformatique.kubik.jasper.service;

import com.cspinformatique.kubik.model.sales.Invoice;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
	JasperPrint generateInvoiceReport(Invoice invoice);
	
	JasperPrint generateReceiptReport(Invoice invoice);
}
