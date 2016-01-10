package com.cspinformatique.kubik.server.domain.sales.exception;

import com.braintreegateway.Transaction;

public class InvalidTransactionStatus extends RuntimeException {
	private static final long serialVersionUID = 8332170708014584814L;
	
	private final Transaction transaction;
	
	public InvalidTransactionStatus(final Transaction transaction){
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}
}
