package com.cspinformatique.kubik.jasper.service;

import com.cspinformatique.kubik.sales.model.Invoice;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
	JasperPrint generateInvoiceReport(Invoice invoice);
	
	JasperPrint generateReceiptReport(Invoice invoice);
}
