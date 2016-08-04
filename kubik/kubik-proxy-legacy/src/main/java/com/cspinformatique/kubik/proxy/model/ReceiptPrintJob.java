package com.cspinformatique.kubik.proxy.model;

public class ReceiptPrintJob {
	private int id;
	private Invoice invoice;
	
	public ReceiptPrintJob(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
}
