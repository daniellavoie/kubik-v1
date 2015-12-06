package com.cspinformatique.kubik.server.jasper.service;

import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.sales.Invoice;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
	JasperPrint generatePurchaseOrderReport(PurchaseOrder purchaseOrder);
	
	JasperPrint generateReceiptReport(Invoice invoice);
	
	JasperPrint generateRmaReport(Rma rma);
}
