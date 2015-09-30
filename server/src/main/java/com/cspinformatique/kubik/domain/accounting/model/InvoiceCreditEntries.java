package com.cspinformatique.kubik.domain.accounting.model;

public class InvoiceCreditEntries {
	private double taxRate;
	private Entry taxAccountEntry;
	private Entry productAccountEntry;

	public InvoiceCreditEntries() {

	}

	public InvoiceCreditEntries(double taxRate, Entry taxAccountEntry, Entry productAccountEntry) {
		this.taxRate = taxRate;
		this.taxAccountEntry = taxAccountEntry;
		this.productAccountEntry = productAccountEntry;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public Entry getTaxAccountEntry() {
		return taxAccountEntry;
	}

	public void setTaxAccountEntry(Entry taxAccountEntry) {
		this.taxAccountEntry = taxAccountEntry;
	}

	public Entry getProductAccountEntry() {
		return productAccountEntry;
	}

	public void setProductAccountEntry(Entry productAccountEntry) {
		this.productAccountEntry = productAccountEntry;
	}
}
