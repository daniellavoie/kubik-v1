package com.cspinformatique.kubik.server.model.sales;

public class ProductInvoice {
	private InvoiceDetail invoiceDetail;
	private Invoice invoice;
	
	public ProductInvoice(){
		
	}

	public ProductInvoice(InvoiceDetail invoiceDetail, Invoice invoice) {
		this.invoiceDetail = invoiceDetail;
		this.invoice = invoice;
	}

	public InvoiceDetail getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(InvoiceDetail invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
}
