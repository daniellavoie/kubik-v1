package com.cspinformatique.kubik.server.domain.sales.exception;

import java.util.List;

import com.braintreegateway.ValidationError;

public class PaymentGatewayException extends RuntimeException {
	private static final long serialVersionUID = 5365461510171950086L;

	public PaymentGatewayException(List<ValidationError> validationErrors) {
		this(validationErrors.stream().map(e -> e.getMessage() + "\n").reduce("", String::concat).trim());
	}

	private PaymentGatewayException(String message) {
		super(message);
	}

}
