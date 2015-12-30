package com.cspinformatique.kubik.kos.model.account;

import java.util.List;

import com.cspinformatique.kubik.kos.model.Wrapper;

public class AccountWrapper extends Wrapper {
	private Account account;
	
	public AccountWrapper(){
		
	}
	
	public AccountWrapper(Account account, List<String> errors){
		super(errors);
		
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
