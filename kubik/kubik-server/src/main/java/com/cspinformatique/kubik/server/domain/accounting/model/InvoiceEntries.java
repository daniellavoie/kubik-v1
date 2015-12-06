package com.cspinformatique.kubik.server.domain.accounting.model;

import java.util.List;

public class InvoiceEntries {
	private Entry debitEntry;
	private List<InvoiceCreditEntries> creditEntries;
	
	public InvoiceEntries(){
		
	}
	
	public InvoiceEntries(Entry debitEntry, List<InvoiceCreditEntries> creditEntries) {
		this.debitEntry = debitEntry;
		this.creditEntries = creditEntries;
	}

	public Entry getDebitEntry() {
		return debitEntry;
	}

	public void setDebitEntry(Entry debitEntry) {
		this.debitEntry = debitEntry;
	}

	public List<InvoiceCreditEntries> getCreditEntries() {
		return creditEntries;
	}

	public void setCreditEntries(List<InvoiceCreditEntries> creditEntries) {
		this.creditEntries = creditEntries;
	}
}
