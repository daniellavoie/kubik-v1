package com.cspinformatique.kubik.jasper.service;

import net.sf.jasperreports.engine.JasperPrint;

import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.Rma;
import com.cspinformatique.kubik.model.sales.Invoice;

public interface ReportService {
	JasperPrint generatePurchaseOrderReport(PurchaseOrder purchaseOrder);
	
	JasperPrint generateReceiptReport(Invoice invoice);
	
	JasperPrint generateRmaReport(Rma rma);
}
