package com.cspinformatique.kubik.server.domain.accounting.model;

import java.util.List;

public class CustomerCreditEntries {
	private List<CustomerCreditDebitEntries> debitEntries;
	private Entry creditEntry;
	
	public CustomerCreditEntries(){
		
	}

	public CustomerCreditEntries(List<CustomerCreditDebitEntries> debitEntries, Entry creditEntry) {
		this.debitEntries = debitEntries;
		this.creditEntry = creditEntry;
	}

	public List<CustomerCreditDebitEntries> getDebitEntries() {
		return debitEntries;
	}

	public void setDebitEntries(List<CustomerCreditDebitEntries> debitEntries) {
		this.debitEntries = debitEntries;
	}

	public Entry getCreditEntry() {
		return creditEntry;
	}

	public void setCreditEntry(Entry creditEntry) {
		this.creditEntry = creditEntry;
	}
}
