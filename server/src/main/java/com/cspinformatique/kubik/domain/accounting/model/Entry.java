package com.cspinformatique.kubik.domain.accounting.model;

import java.util.Date;

public class Entry {
	private Date date;
	private String journalCode;
	private String account;
	private String invoiceNumber;
	private String description;
	private double credit;
	private double debit;
	private String currency;
	
	public Entry() {
		
	}

	public Entry(Date date, String journalCode, String account,
			String invoiceNumber, String description, double credit,
			double debit, String currency) {
		this.date = date;
		this.journalCode = journalCode;
		this.account = account;
		this.invoiceNumber = invoiceNumber;
		this.description = description;
		this.credit = credit;
		this.debit = debit;
		this.currency = currency;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getJournalCode() {
		return journalCode;
	}

	public void setJournalCode(String journalCode) {
		this.journalCode = journalCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
