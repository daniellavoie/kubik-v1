package com.cspinformatique.kubik.server.domain.sales.exception;

import org.springframework.http.HttpStatus;

import com.braintreegateway.Transaction;
import com.cspinformatique.kubik.common.rest.RestException;
import com.cspinformatique.kubik.kos.model.RestErrors;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

public class TransactionDotNotMatchOrderException extends RestException {
	private static final long serialVersionUID = -97070062840240444L;

	private final Transaction transaction;
	private final CustomerOrder customerOrder;

	public TransactionDotNotMatchOrderException(final Transaction transaction, final CustomerOrder customerOrder) {
		super("La commande " + customerOrder.getId()
				+ " ne peut etre traitée tant que le montant total n'égale pas celui du règlement " + transaction.getId()
				+ ".", HttpStatus.INTERNAL_SERVER_ERROR, RestErrors.TRANSACTION_DOES_NOT_MATCH_ORDER.getCode());

		this.transaction = transaction;
		this.customerOrder = customerOrder;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public CustomerOrder getCustomerOrder() {
		return customerOrder;
	}
}
