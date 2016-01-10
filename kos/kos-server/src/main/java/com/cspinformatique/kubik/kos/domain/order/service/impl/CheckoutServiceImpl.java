package com.cspinformatique.kubik.kos.domain.order.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.cspinformatique.kubik.kos.domain.order.exception.TransactionException;
import com.cspinformatique.kubik.kos.domain.order.service.CheckoutService;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {
	@Resource
	private BraintreeGateway braintreeGateway;

	@Override
	public void checkout(CustomerOrder customerOrder, String nonce) {
		if (customerOrder.getCustomerOrderDetails().isEmpty())
			throw new RuntimeException("Cart is empty");

		if (customerOrder.getAccount() == null)
			throw new RuntimeException("Order is not linked to any customer.");

		if (customerOrder.getTotalAmount() < 0d)
			throw new RuntimeException("Order total amount should be over 0.");

		TransactionRequest request = new TransactionRequest()
				.amount(new BigDecimal(customerOrder.getTotalAmount().toString())).paymentMethodNonce(nonce);

		Result<Transaction> result = braintreeGateway.transaction().sale(request);

		if (result.getErrors() != null)
			throw new TransactionException(result.getMessage(), result);

		Transaction transaction = result.getTarget();

		Assert.notNull(transaction);

		String transactionId = transaction.getId();
		Assert.notNull(transactionId);

		customerOrder.setStatus(CustomerOrder.Status.CONFIRMED);
		customerOrder.setTransactionId(transactionId);
	}

	@Override
	public String generateClientToken() {
		return braintreeGateway.clientToken().generate();
	}
}
