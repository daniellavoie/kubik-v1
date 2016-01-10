package com.cspinformatique.kubik.server.domain.sales.service;

import com.braintreegateway.Transaction;
import com.cspinformatique.kubik.server.model.sales.Payment;

public interface PaymentService {
	String getGatewayAdminUrl();
	
	String getGatewayMerchantId();
	
	Transaction loadTransaction(String transactionId);
	
	Iterable<Payment> save(Iterable<Payment> payments);
	
	void refundPaymentGatewayTransaction(String transactionId);
	
	void settlePaymentGatewayTransaction(String transactionId);
	
	void voidPaymentGatewayTransaction(String transactionId);
}
