package com.cspinformatique.kubik.kos.domain.order.exception;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;

public class TransactionException extends RuntimeException {
	private static final long serialVersionUID = -367107834412138094L;

	private final Result<Transaction> result;

	public TransactionException(String message, Result<Transaction> result) {
		super(message);

		this.result = result;
	}

	public Result<Transaction> getResult() {
		return result;
	}
}
