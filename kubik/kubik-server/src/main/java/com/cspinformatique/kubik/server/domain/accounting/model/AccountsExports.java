package com.cspinformatique.kubik.server.domain.accounting.model;

import java.util.List;

public class AccountsExports {
	private List<Account> accounts;
	private String label;
	private String separator;
	
	public AccountsExports(){
		
	}

	public AccountsExports(List<Account> accounts, String label, String separator) {
		this.accounts = accounts;
		this.label = label;
		this.separator = separator;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
}
