package com.cspinformatique.kubik.jasper.service;

import com.cspinformatique.kubik.domain.purchase.model.Rma;
import com.cspinformatique.kubik.model.sales.Invoice;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {	
	JasperPrint generateReceiptReport(Invoice invoice);
	
	JasperPrint generateRmaReport(Rma rma);
}
