package com.cspinformatique.kubik.kos.domain.account.controller.wrapper;

import java.util.List;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.rest.Wrapper;

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
