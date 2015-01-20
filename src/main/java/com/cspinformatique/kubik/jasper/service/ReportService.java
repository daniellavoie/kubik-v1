package com.cspinformatique.kubik.jasper.service;

import com.cspinformatique.kubik.sales.model.Invoice;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
	public JasperPrint generateInvoiceReport(Invoice invoice);
	
	public JasperPrint generateReceiptReport(Invoice invoice);
}
