package com.cspinformatique.kubik.domain.accounting.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.accounting.model.Entry;
import com.cspinformatique.kubik.domain.accounting.model.EntryComparator;
import com.cspinformatique.kubik.domain.accounting.service.EntryService;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.model.sales.InvoiceTaxAmount;
import com.cspinformatique.kubik.model.sales.Payment;
import com.cspinformatique.kubik.model.sales.PaymentMethod;

@Service
public class EntryServiceImpl implements EntryService, InitializingBean {
	private static final String ANONYMUS_CLIENT = "ANONYME";
	private static final String CUSTOMER_ACCOUNT_PREFIX = "411";
	private static final String PRODUCT_ACCOUNT_PREFIX = "7070";
	private static final String SALES_ACCOUNT_PREFIX = "580";
	private static final String TAX_ACCOUNT_PREFIX = "4457";

	@Autowired
	private CustomerCreditService customerCreditService;

	@Autowired
	private InvoiceService invoiceService;

	private DecimalFormat decimalFormat;
	private EntryComparator entryComparator;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.decimalFormat = new DecimalFormat("0.0");
		this.entryComparator = new EntryComparator();
	}

	@Override
	public List<Entry> generateSaleJournalEntries(Date startDate, Date endDate) {
		Page<Invoice> invoicePage = null;
		List<Entry> entries = new ArrayList<Entry>();
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC,
				"paidDate");

		do {
			invoicePage = this.invoiceService.findByPaidDateBetweenAndStatus(
					startDate, endDate, new InvoiceStatus(
							InvoiceStatus.Types.PAID.toString(), null),
					pageRequest);

			for (Invoice invoice : invoicePage.getContent()) {
				// Generates a debit entry for the customer.
				String customerName = this.getCustomerName(invoice, 16);

				entries.add(new Entry(invoice.getPaidDate(), "vt", this
						.getCustomerAccount(invoice), "F" + invoice.getNumber(),
						StringUtils.stripAccents("VENTE " + customerName), invoice
								.getTotalAmount(), 0d, ""));

				// Generates a credit entry for each product account.
				HashMap<Double, Double> productAccountAmounts = new HashMap<Double, Double>();
				for (InvoiceDetail invoiceDetail : invoice.getDetails()) {
					Double productsAmount = productAccountAmounts
							.get(invoiceDetail.getProduct().getTvaRate1());
					if (productsAmount == null) {
						productsAmount = 0d;
					}
					productsAmount += invoiceDetail.getTotalTaxLessAmount();

					productAccountAmounts.put(invoiceDetail.getProduct()
							.getTvaRate1(), productsAmount);
				}

				for (java.util.Map.Entry<Double, Double> productAmountEntry : productAccountAmounts
						.entrySet()) {
					entries.add(new Entry(
							invoice.getPaidDate(),
							"vt",
							PRODUCT_ACCOUNT_PREFIX
									+ decimalFormat
											.format(productAmountEntry.getKey())
											.replace(".", "").substring(0, 2),
							"F" + invoice.getNumber(), StringUtils.stripAccents("VENTE " + customerName), 0d, productAmountEntry
									.getValue(), ""));
				}

				// Generate a credit entry for each TVA rate.
				for (InvoiceTaxAmount invoiceTaxAmount : invoice
						.getTaxesAmounts().values()) {
					decimalFormat.format(invoiceTaxAmount.getTaxRate())
							.substring(0, 2);

					entries.add(new Entry(invoice.getPaidDate(), "vt",
							TAX_ACCOUNT_PREFIX
									+ decimalFormat
											.format(invoiceTaxAmount
													.getTaxRate())
											.replace(".", "").substring(0, 2),
							"F" + invoice.getNumber(), StringUtils.stripAccents("VENTE " + customerName), 0d, invoiceTaxAmount
									.getTaxAmount(), ""));
				}
			}

			pageRequest = pageRequest.next();
		} while (invoicePage != null && invoicePage.getContent().size() != 0);

		Page<CustomerCredit> customerCreditPage = null;
		pageRequest = new PageRequest(0, 100, Direction.ASC, "completeDate");
		do {
			customerCreditPage = this.customerCreditService
					.findByCompleteDateBetweenAndStatus(startDate, endDate,
							CustomerCredit.Status.COMPLETED, pageRequest);

			for (CustomerCredit customerCredit : customerCreditPage
					.getContent()) {
				// Generates a credit entry for the customer.
				String customerName = this.getCustomerName(customerCredit, 14);

				entries.add(new Entry(customerCredit.getCompleteDate(), "rt",
						this.getCustomerAccount(customerCredit), "A" + customerCredit
								.getInvoice().getNumber(), StringUtils.stripAccents("RETOUR "
								+ customerName), 0d,
						customerCredit.getTotalAmount(), "E"));

				// Generates a debit entry for each product account.
				HashMap<Double, Double> productAccountAmounts = new HashMap<Double, Double>();
				for (CustomerCreditDetail customerCreditDetail : customerCredit
						.getDetails()) {
					Double productsAmount = productAccountAmounts
							.get(customerCreditDetail.getProduct()
									.getTvaRate1());
					if (productsAmount == null) {
						productsAmount = 0d;
					}
					productsAmount += customerCreditDetail
							.getTotalTaxLessAmount();

					productAccountAmounts.put(customerCreditDetail.getProduct()
							.getTvaRate1(), productsAmount);
				}

				for (java.util.Map.Entry<Double, Double> productAmountEntry : productAccountAmounts
						.entrySet()) {
					entries.add(new Entry(
							customerCredit.getCompleteDate(),
							"rt",
							PRODUCT_ACCOUNT_PREFIX
									+ decimalFormat
											.format(productAmountEntry.getKey())
											.replace(".", "").substring(0, 2),
											"A" + customerCredit.getInvoice().getNumber(), StringUtils.stripAccents("RETOUR "
									+ customerName),
							productAmountEntry.getValue(), 0d, "E"));
				}

				// Generate a debit entry for each TVA rate.
				for (InvoiceTaxAmount invoiceTaxAmount : customerCredit
						.getTaxesAmounts().values()) {
					decimalFormat.format(invoiceTaxAmount.getTaxRate())
							.substring(0, 2);

					entries.add(new Entry(customerCredit.getCompleteDate(),
							"rt", TAX_ACCOUNT_PREFIX
									+ decimalFormat
											.format(invoiceTaxAmount
													.getTaxRate())
											.replace(".", "").substring(0, 2),
											"A" + customerCredit.getInvoice().getNumber(), StringUtils.stripAccents("RETOUR "
									+ customerName),
							invoiceTaxAmount.getTaxAmount(), 0d, "E"));
				}
			}

			pageRequest = pageRequest.next();
		} while (customerCreditPage != null
				&& customerCreditPage.getContent().size() != 0);

		Collections.sort(entries, this.entryComparator);
		
		return entries;
	}

	@Override
	public List<Entry> generateTreasuryJournalEntries(Date startDate,
			Date endDate) {
		Page<Invoice> invoicePage = null;
		List<Entry> entries = new ArrayList<Entry>();
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC,
				"paidDate");

		do {
			invoicePage = this.invoiceService.findByPaidDateBetweenAndStatus(
					startDate, endDate, new InvoiceStatus(
							InvoiceStatus.Types.PAID.toString(), null),
					pageRequest);

			// CHEQUE, ESPECE, CARTE
			for (Invoice invoice : invoicePage.getContent()) {
				for (Payment payment : invoice.getPayments()) {
					if (!payment.getPaymentMethod().getType()
							.equals(PaymentMethod.Types.CREDIT)) {
						// Generates a debit entry for the sales account.
						entries.add(new Entry(
								invoice.getPaidDate(),
								"op",
								SALES_ACCOUNT_PREFIX + payment
								.getPaymentMethod().getAccountingCode(),
								"F" + invoice.getNumber(),
								"REGLEMENT "
										+ this.getCustomerName(invoice, 11)
										+ " - "
										+ payment.getPaymentMethod()
												.getDescription().toUpperCase(),
								0d, payment.getAmount(), "E"));

						// Generates a credit entry for the customer account.
						entries.add(new Entry(invoice.getPaidDate(), "op", this
								.getCustomerAccount(invoice), "F" + invoice
								.getNumber(), StringUtils.stripAccents("REGLEMENT "
								+ this.getCustomerName(invoice, 11)
								+ " - "
								+ payment.getPaymentMethod().getDescription()
										.toUpperCase()), payment.getAmount(),
								0d, "E"));
					}
				}
			}

			pageRequest = pageRequest.next();
		} while (invoicePage != null && invoicePage.getContent().size() != 0);

		Page<CustomerCredit> customerCreditPage = null;
		pageRequest = new PageRequest(0, 100, Direction.ASC, "completeDate");

		do {
			customerCreditPage = this.customerCreditService
					.findByCompleteDateBetweenAndStatus(startDate, endDate,
							CustomerCredit.Status.COMPLETED, pageRequest);

			// CHEQUE, ESPECE, CARTE
			for (CustomerCredit customerCredit : customerCreditPage
					.getContent()) {
				if (!customerCredit.getPaymentMethod().getType()
						.equals(PaymentMethod.Types.CREDIT)) {
					// Generates a credit entry for the sales account.
					entries.add(new Entry(customerCredit.getCompleteDate(),
							"op", SALES_ACCOUNT_PREFIX + customerCredit
							.getPaymentMethod().getAccountingCode(), "A" + customerCredit.getInvoice()
									.getNumber(), StringUtils.stripAccents("REMB."
									+ this.getCustomerName(customerCredit, 16)
									+ " - "
									+ customerCredit.getPaymentMethod()
											.getDescription().toUpperCase()),
							customerCredit.getTotalAmount(), 0d, "E"));

					// Generates a debit entry for the customer account.
					entries.add(new Entry(customerCredit.getCompleteDate(),
							"op", this.getCustomerAccount(customerCredit),
							"A" + customerCredit.getInvoice().getNumber(), StringUtils.stripAccents("REMB. "
									+ this.getCustomerName(customerCredit, 16)
									+ " - "
									+ customerCredit.getPaymentMethod()
											.getDescription().toUpperCase()),
							0d, customerCredit.getTotalAmount(), "E"));
				}
			}

			pageRequest = pageRequest.next();
		} while (customerCreditPage != null
				&& customerCreditPage.getContent().size() != 0);

		Collections.sort(entries, this.entryComparator);
		
		return entries;
	}

	private String getCustomerAccount(Invoice invoice) {
		String customerAccount = CUSTOMER_ACCOUNT_PREFIX + "9999";

		if (invoice.getCustomer() != null) {
			customerAccount = CUSTOMER_ACCOUNT_PREFIX
					+ String.format("%04d", invoice.getCustomer().getId());
		}

		return customerAccount;
	}

	private String getCustomerAccount(CustomerCredit customerCredit) {
		return CUSTOMER_ACCOUNT_PREFIX
				+ String.format("%05d", customerCredit.getCustomer().getId());
	}

	private String getCustomerName(Invoice invoice, int length) {
		String customerName = ANONYMUS_CLIENT;

		if (invoice.getCustomer() != null) {
			String lastName = invoice.getCustomer().getLastName();
			if (lastName.length() > length) {
				lastName = lastName.substring(0, length);
			}
			customerName = StringUtils.upperCase(lastName);
		}

		return customerName;
	}

	private String getCustomerName(CustomerCredit customerCredit, int length) {
		String customerName = customerCredit.getCustomer().getLastName();

		if (customerName.length() > length) {
			customerName = customerName.substring(0, length);
		}

		return StringUtils.upperCase(customerName);
	}
}
