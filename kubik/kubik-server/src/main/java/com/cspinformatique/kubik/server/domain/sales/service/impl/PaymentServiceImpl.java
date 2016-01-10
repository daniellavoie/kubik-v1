package com.cspinformatique.kubik.server.domain.sales.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.cspinformatique.kubik.server.domain.sales.exception.PaymentGatewayException;
import com.cspinformatique.kubik.server.domain.sales.repository.PaymentRepository;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentService;
import com.cspinformatique.kubik.server.model.sales.Payment;

@Service
@PropertySource(value = "braintree.properties")
public class PaymentServiceImpl implements PaymentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Resource
	BraintreeGateway braintreeGateway;

	@Autowired
	PaymentRepository paymentRepository;

	@Value("${braintree.admin.url}")
	String gatewayAdminUrl;

	@Value("${braintree.merchant.id}")
	String merchantId;

	@Override
	public String getGatewayAdminUrl() {
		return gatewayAdminUrl;
	}

	@Override
	public String getGatewayMerchantId() {
		return merchantId;
	}

	@Override
	public Transaction loadTransaction(String transactionId) {
		return braintreeGateway.transaction().find(transactionId);
	}

	@Override
	public Iterable<Payment> save(Iterable<Payment> payments) {
		return this.paymentRepository.save(payments);
	}

	@Override
	public void refundPaymentGatewayTransaction(String transactionId) {
		LOGGER.info("Submitting transaction " + transactionId + " for refund.");

		Result<Transaction> result = braintreeGateway.transaction().refund(transactionId);

		if (!result.isSuccess()) {
			throw new PaymentGatewayException(result.getErrors().getAllDeepValidationErrors());
		}
	}

	@Override
	public void settlePaymentGatewayTransaction(String transactionId) {
		LOGGER.info("Submitting transaction " + transactionId + " for settlement.");

		Result<Transaction> result = braintreeGateway.transaction().submitForSettlement(transactionId);

		if (!result.isSuccess()) {
			throw new PaymentGatewayException(result.getErrors().getAllDeepValidationErrors());
		}
	}

	@Override
	public void voidPaymentGatewayTransaction(String transactionId) {
		LOGGER.info("Voiding transaction " + transactionId + ".");
		Result<Transaction> result = braintreeGateway.transaction().voidTransaction(transactionId);

		if (!result.isSuccess()) {
			throw new PaymentGatewayException(result.getErrors().getAllDeepValidationErrors());
		}
	}
}
