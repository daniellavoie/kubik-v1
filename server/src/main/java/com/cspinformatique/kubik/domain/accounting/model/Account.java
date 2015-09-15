package com.cspinformatique.kubik.domain.accounting.model;

public class Account {
	private String accountNumber;
	private String customerName;
	
	public Account() {
		
	}

	public Account(String accountNumber, String customerName) {
		this.accountNumber = accountNumber;
		this.customerName = customerName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
