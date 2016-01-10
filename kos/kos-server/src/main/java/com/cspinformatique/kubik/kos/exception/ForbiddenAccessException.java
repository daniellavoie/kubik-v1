package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.HttpStatus;

import com.cspinformatique.kubik.common.rest.RestException;
import com.cspinformatique.kubik.kos.model.account.Account;

public class ForbiddenAccessException extends RestException {
	private static final long serialVersionUID = 5564428887240758106L;

	private Account account;

	public ForbiddenAccessException(Account account, String message) {
		super(message, HttpStatus.FORBIDDEN);

		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

}
