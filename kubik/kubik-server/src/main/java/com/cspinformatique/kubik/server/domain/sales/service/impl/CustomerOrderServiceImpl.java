package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.braintreegateway.Transaction;
import com.cspinformatique.kubik.common.rest.RestPage;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.Status;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;
import com.cspinformatique.kubik.server.domain.kos.rest.KosTemplate;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.sales.exception.InvalidTransactionStatus;
import com.cspinformatique.kubik.server.domain.sales.exception.TransactionDotNotMatchOrderException;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerOrderService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentMethodService;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.server.model.sales.Payment;
import com.cspinformatique.kubik.server.model.sales.PaymentMethod;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(CustomerOrderServiceImpl.class);

	private static final String CUSTOMER_ORDER_RESOURCE = "/customer-order";

	private static final List<Transaction.Status> VALID_TRANSACTION_STATES = Arrays.asList(
			new Transaction.Status[] { Transaction.Status.AUTHORIZED, Transaction.Status.SUBMITTED_FOR_SETTLEMENT,
					Transaction.Status.SETTLING, Transaction.Status.SETTLEMENT_PENDING,
					Transaction.Status.SETTLEMENT_CONFIRMED, Transaction.Status.SETTLED });

	@Resource
	InvoiceService invoiceService;

	@Resource
	InvoiceStatusService invoiceStatusService;

	@Resource
	PaymentService paymentService;

	@Resource
	PaymentMethodService paymentMethodService;

	@Resource
	ProductService productService;

	@Resource
	KosTemplate kosTemplate;

	@Override
	public Page<CustomerOrder> findAll(MultiValueMap<String, String> parameters) {
		return kosTemplate.exchange(CUSTOMER_ORDER_RESOURCE, parameters, HttpMethod.GET, new CustomerOrderPageType());
	}

	@Override
	public CustomerOrder findOne(long id) {
		return kosTemplate.exchange(CUSTOMER_ORDER_RESOURCE + "/" + id, HttpMethod.GET, CustomerOrder.class);
	}

	private void processCustomerOrder(CustomerOrder customerOrder) {
		// Generate an invoice.
		Invoice invoice = invoiceService.generateNewInvoice(customerOrder.getId(), customerOrder.getShippingCost());

		// Add details to invoice.
		for (CustomerOrderDetail customerOrderDetail : customerOrder.getCustomerOrderDetails()) {
			Product product = productService.findOne(customerOrderDetail.getProduct().getKubikId());

			invoice.getDetails()
					.add(new InvoiceDetail(null, invoice, product, (double) customerOrderDetail.getQuantityShipped(),
							0d, null, product.getPriceTaxIn(), 0d, 0d, 0d, 0d));
		}

		// Validate the transaction state.
		Transaction transaction = validateTransactionState(customerOrder);

		if (Transaction.Status.AUTHORIZED.equals(transaction.getStatus())) {
			// Confirm payment to payment gateway.
			paymentService.settlePaymentGatewayTransaction(customerOrder.getTransactionId());
		}

		// Add payment to invoice.
		invoice.getPayments().add(new Payment(0, invoice, customerOrder.getTotalAmount(),
				paymentMethodService.findByType(PaymentMethod.Types.BRAINTREE.name())));

		paymentService.save(invoice.getPayments());

		// Close the invoice.
		invoice.setStatus(invoiceStatusService.findByType(InvoiceStatus.Types.PAID.name()));

		// Persists current state of the invoice to close it and to calculate
		// every amounts.
		invoice = invoiceService.save(invoice);

		// Flag the customer order as closed.
		customerOrder.setProcessedDate(new Date());

		// } catch (RuntimeException runtimeEx) {
		// // Voids the transactions that has been previously settled.
		// try {
		// paymentService.voidPaymentGatewayTransaction(customerOrder.getTransactionId());
		// } catch (RuntimeException runtimeEx2) {
		// LOGGER.error(
		// "TRANSACTION " + customerOrder.getTransactionId() + " for customer
		// order "
		// + customerOrder.getId() + "COULD NOT BE REFUNDED, INVESTIGATE
		// IMMEDIATELY !!!",
		// runtimeEx2);
		// }
		//
		// throw runtimeEx;
		// }
	}

	@Override
	@Transactional
	public CustomerOrder save(CustomerOrder customerOrder) {
		if (Status.PROCESSED.equals(customerOrder.getStatus()) && customerOrder.getProcessedDate() == null)
			processCustomerOrder(customerOrder);

		return kosTemplate.exchange(CUSTOMER_ORDER_RESOURCE, HttpMethod.POST, customerOrder, CustomerOrder.class);
	}

	private class CustomerOrderPageType extends ParameterizedTypeReference<RestPage<CustomerOrder>> {

	}

	private Transaction validateTransactionState(CustomerOrder customerOrder) {
		Transaction transaction = paymentService.loadTransaction(customerOrder.getTransactionId());

		if (Precision.round(transaction.getAmount().doubleValue(), 2) != customerOrder.getTotalAmount()) {
			throw new TransactionDotNotMatchOrderException(transaction, customerOrder);
		}

		if (!VALID_TRANSACTION_STATES.contains(transaction.getStatus())) {
			throw new InvalidTransactionStatus(transaction);
		}

		return transaction;
	}
}
