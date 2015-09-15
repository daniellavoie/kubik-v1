package com.cspinformatique.kubik.domain.accounting.service.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.accounting.model.Account;
import com.cspinformatique.kubik.domain.accounting.model.Entry;
import com.cspinformatique.kubik.domain.accounting.model.EntryComparator;
import com.cspinformatique.kubik.domain.accounting.service.EntryService;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.model.sales.Customer;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(EntryServiceImpl.class);

	private static final String ANONYMUS_CLIENT = "ANONYME";
	private static final String CUSTOMER_ACCOUNT_PREFIX = "411";
	private static final String PRODUCT_ACCOUNT_PREFIX = "7070";
	private static final String SALES_ACCOUNT_PREFIX = "58";
	private static final String TAX_ACCOUNT_PREFIX = "4457";

	@Autowired
	private CustomerCreditService customerCreditService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InvoiceService invoiceService;

	private DecimalFormat decimalFormat;
	private DecimalFormatSymbols decimalFormatSymbols;
	private EntryComparator entryComparator;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.decimalFormat = new DecimalFormat("0.0");
		this.decimalFormatSymbols = new DecimalFormatSymbols();
		this.entryComparator = new EntryComparator();
	}

	@Override
	public List<Account> generateAccounts() {
		List<Account> accounts = new ArrayList<>();

		Pageable pageable = new PageRequest(0, 50);
		Page<Customer> customersPage = null;
		do {
			customersPage = customerService.findAll(pageable);
			for (Customer customer : customersPage.getContent()) {
				accounts.add(new Account(getCustomerAccount(customer),
						customer.getFirstName() + " " + customer.getLastName()));
			}

			pageable = pageable.next();
		} while (customersPage.hasNext());

		return accounts;
	}

	private Entry generateCustomerCreditEntry(CustomerCredit customerCredit) {
		return new Entry(customerCredit.getCompleteDate(), "rt", this.getCustomerAccount(customerCredit),
				"F" + customerCredit.getInvoice().getNumber(),
				StringUtils.stripAccents("RETOUR " + this.getCustomerName(customerCredit, 14)), 0d,
				customerCredit.getTotalAmount(), "E");
	}

	private Entry generateCustomerDebitEntry(Invoice invoice) {
		String customerName = this.getCustomerName(invoice, 16);

		Entry customerDebitEntry = new Entry(invoice.getPaidDate(), "vt", this.getCustomerAccount(invoice),
				"F" + invoice.getNumber(), StringUtils.stripAccents("VENTE " + customerName), invoice.getTotalAmount(),
				0d, "E");

		LOGGER.debug("Generating a new debit entry for invoice " + invoice.getId() + " to customer account "
				+ customerDebitEntry.getAccount() + ".");

		return customerDebitEntry;
	}

	private List<Entry> generateProductAccountCreditEntries(Invoice invoice) {
		List<Entry> entries = new ArrayList<Entry>();

		HashMap<Double, Double> productAccountAmounts = new HashMap<Double, Double>();
		for (InvoiceDetail invoiceDetail : invoice.getDetails()) {
			Double productsAmount = productAccountAmounts.get(invoiceDetail.getProduct().getTvaRate1());
			if (productsAmount == null) {
				productsAmount = 0d;
			}
			productsAmount += invoiceDetail.getTotalTaxLessAmount();

			LOGGER.debug("Product " + invoiceDetail.getProduct().getId() + " assigned to tax account "
					+ invoiceDetail.getProduct().getTvaRate1());

			productAccountAmounts.put(invoiceDetail.getProduct().getTvaRate1(), productsAmount);
		}

		for (java.util.Map.Entry<Double, Double> productAmountEntry : productAccountAmounts.entrySet()) {
			String productAccountSufix = decimalFormat.format(productAmountEntry.getKey())
					.replace(decimalFormatSymbols.getDecimalSeparator() + "", "");

			productAccountSufix = productAccountSufix.substring(productAccountSufix.length() - 2,
					productAccountSufix.length());

			String productAccount = PRODUCT_ACCOUNT_PREFIX + productAccountSufix;

			Entry productAccountCreditEntry = new Entry(invoice.getPaidDate(), "vt", productAccount,
					"F" + invoice.getNumber(), StringUtils.stripAccents("VENTE " + this.getCustomerName(invoice, 16)),
					0d, productAmountEntry.getValue(), "E");

			LOGGER.debug("Generating a credit entry for invoice " + invoice.getId() + " to product account "
					+ productAccount + ".");

			entries.add(productAccountCreditEntry);
		}

		return entries;
	}

	private List<Entry> generateProductAccountDebitEntries(CustomerCredit customerCredit) {
		List<Entry> entries = new ArrayList<Entry>();
		HashMap<Double, Double> productAccountAmounts = new HashMap<Double, Double>();
		for (CustomerCreditDetail customerCreditDetail : customerCredit.getDetails()) {
			Double productsAmount = productAccountAmounts.get(customerCreditDetail.getProduct().getTvaRate1());
			if (productsAmount == null) {
				productsAmount = 0d;
			}
			productsAmount += customerCreditDetail.getTotalTaxLessAmount();

			productAccountAmounts.put(customerCreditDetail.getProduct().getTvaRate1(), productsAmount);
		}

		for (java.util.Map.Entry<Double, Double> productAmountEntry : productAccountAmounts.entrySet()) {
			entries.add(
					new Entry(customerCredit.getCompleteDate(), "rt",
							PRODUCT_ACCOUNT_PREFIX + decimalFormat.format(productAmountEntry.getKey()).replace(".", "")
									.substring(0, 2),
							"F" + customerCredit.getInvoice().getNumber(),
							StringUtils.stripAccents("RETOUR " + this.getCustomerName(customerCredit, 14)),
							productAmountEntry.getValue(), 0d, "E"));
		}

		return entries;
	}

	@Override
	public List<Entry> generateSaleJournalEntries(Date startDate, Date endDate) {
		Page<Invoice> invoicePage = null;
		List<Entry> entries = new ArrayList<Entry>();
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "paidDate");

		do {
			invoicePage = this.invoiceService.findByPaidDateBetweenAndStatus(startDate, endDate,
					new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null), pageRequest);

			for (Invoice invoice : invoicePage.getContent()) {
				// Generates a debit entry for the customer.
				entries.add(this.generateCustomerDebitEntry(invoice));

				// Generates a credit entry for each product account.
				entries.addAll(this.generateProductAccountCreditEntries(invoice));

				// Generate a credit entry for each TVA rate.
				entries.addAll(this.generateTaxAcountCreditEntries(invoice));
			}

			pageRequest = pageRequest.next();
		} while (invoicePage != null && invoicePage.getContent().size() != 0);

		Page<CustomerCredit> customerCreditPage = null;
		pageRequest = new PageRequest(0, 100, Direction.ASC, "completeDate");
		do {
			customerCreditPage = this.customerCreditService.findByCompleteDateBetweenAndStatus(startDate, endDate,
					CustomerCredit.Status.COMPLETED, pageRequest);

			for (CustomerCredit customerCredit : customerCreditPage.getContent()) {
				// Generates a credit entry for the customer.
				entries.add(this.generateCustomerCreditEntry(customerCredit));

				// Generates a debit entry for each product account.
				entries.addAll(this.generateProductAccountDebitEntries(customerCredit));

				// Generate a debit entry for each TVA rate.
				entries.addAll(generateTaxAccountDebitEntries(customerCredit));
			}

			pageRequest = pageRequest.next();
		} while (customerCreditPage != null && customerCreditPage.getContent().size() != 0);

		Collections.sort(entries, this.entryComparator);

		return entries;
	}

	private List<Entry> generateTaxAccountDebitEntries(CustomerCredit customerCredit) {
		List<Entry> entries = new ArrayList<Entry>();
		for (InvoiceTaxAmount invoiceTaxAmount : customerCredit.getTaxesAmounts().values()) {
			decimalFormat.format(invoiceTaxAmount.getTaxRate()).substring(0, 2);

			entries.add(
					new Entry(customerCredit.getCompleteDate(), "rt",
							TAX_ACCOUNT_PREFIX + decimalFormat.format(invoiceTaxAmount.getTaxRate()).replace(".", "")
									.substring(0, 2),
							"F" + customerCredit.getInvoice().getNumber(),
							StringUtils.stripAccents("RETOUR " + this.getCustomerName(customerCredit, 14)),
							invoiceTaxAmount.getTaxAmount(), 0d, "E"));
		}

		return entries;
	}

	private List<Entry> generateTaxAcountCreditEntries(Invoice invoice) {
		List<Entry> entries = new ArrayList<Entry>();

		for (InvoiceTaxAmount invoiceTaxAmount : invoice.getTaxesAmounts().values()) {
			decimalFormat.format(invoiceTaxAmount.getTaxRate()).substring(0, 2);

			entries.add(new Entry(invoice.getPaidDate(), "vt",
					TAX_ACCOUNT_PREFIX
							+ decimalFormat.format(invoiceTaxAmount.getTaxRate()).replace(".", "").substring(0, 2),
					"F" + invoice.getNumber(), StringUtils.stripAccents("VENTE " + this.getCustomerName(invoice, 16)),
					0d, invoiceTaxAmount.getTaxAmount(), "E"));
		}

		return entries;
	}

	@Override
	public List<Entry> generateTreasuryJournalEntries(Date startDate, Date endDate) {
		Page<Invoice> invoicePage = null;
		List<Entry> entries = new ArrayList<Entry>();
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "paidDate");

		do {
			invoicePage = this.invoiceService.findByPaidDateBetweenAndStatus(startDate, endDate,
					new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null), pageRequest);

			// CHEQUE, ESPECE, CARTE
			for (Invoice invoice : invoicePage.getContent()) {
				for (Payment payment : invoice.getPayments()) {
					if (!payment.getPaymentMethod().getType().equals(PaymentMethod.Types.CREDIT.name())) {
						// Generates a debit entry for the sales account.
						entries.add(new Entry(invoice.getPaidDate(), "op",
								SALES_ACCOUNT_PREFIX + payment.getPaymentMethod().getAccountingCode(),
								"F" + invoice.getNumber(), "REGLEMENT " + this.getCustomerName(invoice, 14),
								payment.getPaymentMethod().getType().equals(PaymentMethod.Types.CASH)
										? payment.getAmount() - invoice.getAmountReturned() : payment.getAmount(),
								0d, "E"));

						// Generates a credit entry for the customer account.
						entries.add(new Entry(invoice.getPaidDate(), "op", this.getCustomerAccount(invoice),
								"F" + invoice.getNumber(),
								StringUtils.stripAccents("REGLEMENT " + this.getCustomerName(invoice, 16)), 0d,
								payment.getAmount(), "E"));
					}
				}
			}

			pageRequest = pageRequest.next();
		} while (invoicePage != null && invoicePage.getContent().size() != 0);

		Page<CustomerCredit> customerCreditPage = null;
		pageRequest = new PageRequest(0, 100, Direction.ASC, "completeDate");

		do {
			customerCreditPage = this.customerCreditService.findByCompleteDateBetweenAndStatus(startDate, endDate,
					CustomerCredit.Status.COMPLETED, pageRequest);

			// CHEQUE, ESPECE, CARTE
			for (CustomerCredit customerCredit : customerCreditPage.getContent()) {
				if (!customerCredit.getPaymentMethod().getType().equals(PaymentMethod.Types.CREDIT.name())) {
					// Generates a credit entry for the sales account.
					entries.add(new Entry(customerCredit.getCompleteDate(), "op",
							SALES_ACCOUNT_PREFIX + customerCredit.getPaymentMethod().getAccountingCode(),
							"F" + customerCredit.getInvoice().getNumber(),
							StringUtils.stripAccents("REMB." + this.getCustomerName(customerCredit, 16)), 0d,
							customerCredit.getTotalAmount(), "E"));

					// Generates a debit entry for the customer account.
					entries.add(new Entry(customerCredit.getCompleteDate(), "op",
							this.getCustomerAccount(customerCredit), "F" + customerCredit.getInvoice().getNumber(),
							StringUtils.stripAccents("REMB. " + this.getCustomerName(customerCredit, 16)),
							customerCredit.getTotalAmount(), 0d, "E"));
				}
			}

			pageRequest = pageRequest.next();
		} while (customerCreditPage != null && customerCreditPage.getContent().size() != 0);

		Collections.sort(entries, this.entryComparator);

		return entries;
	}

	private String getCustomerAccount(Customer customer) {
		String customerAccount = CUSTOMER_ACCOUNT_PREFIX + "9999";

		if (customer != null) {
			customerAccount = CUSTOMER_ACCOUNT_PREFIX + String.format("%04d", customer.getId());
		}

		return customerAccount;
	}

	private String getCustomerAccount(Invoice invoice) {
		return getCustomerAccount(invoice.getCustomer());
	}

	private String getCustomerAccount(CustomerCredit customerCredit) {
		return this.getCustomerAccount(customerCredit.getInvoice());
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
