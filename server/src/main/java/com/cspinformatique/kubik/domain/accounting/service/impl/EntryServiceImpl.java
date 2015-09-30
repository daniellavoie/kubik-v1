package com.cspinformatique.kubik.domain.accounting.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

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
import com.cspinformatique.kubik.domain.accounting.model.CustomerCreditDebitEntries;
import com.cspinformatique.kubik.domain.accounting.model.CustomerCreditEntries;
import com.cspinformatique.kubik.domain.accounting.model.Entry;
import com.cspinformatique.kubik.domain.accounting.model.EntryComparator;
import com.cspinformatique.kubik.domain.accounting.model.InvoiceCreditEntries;
import com.cspinformatique.kubik.domain.accounting.model.InvoiceEntries;
import com.cspinformatique.kubik.domain.accounting.service.EntryService;
import com.cspinformatique.kubik.domain.accounting.service.TransactionEntriesService;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.model.sales.Customer;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.model.sales.Payment;
import com.cspinformatique.kubik.model.sales.PaymentMethod;

@Service
public class EntryServiceImpl implements EntryService, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(EntryServiceImpl.class);

	private static final String ANONYMUS_CLIENT = "ANONYME";

	@Autowired
	private CustomerCreditService customerCreditService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InvoiceService invoiceService;

	@Resource
	private TransactionEntriesService transactionEntriesService;

	private EntryComparator entryComparator;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.entryComparator = new EntryComparator();
	}

	private List<Entry> convertCustomerCreditEntriesToList(CustomerCreditEntries customerCreditEntries) {
		List<Entry> entries = new ArrayList<>();

		for (CustomerCreditDebitEntries customerCreditDebitEntries : customerCreditEntries.getDebitEntries()) {
			entries.add(customerCreditDebitEntries.getProductAccountEntry());
			entries.add(customerCreditDebitEntries.getTaxAccountEntry());
		}
		entries.add(customerCreditEntries.getCreditEntry());

		return entries;
	}

	private List<Entry> convertInvoiceEntriesToList(InvoiceEntries invoiceEntries) {
		List<Entry> entries = new ArrayList<>();

		entries.add(invoiceEntries.getDebitEntry());
		for (InvoiceCreditEntries invoiceCreditEntries : invoiceEntries.getCreditEntries()) {
			entries.add(invoiceCreditEntries.getProductAccountEntry());
			entries.add(invoiceCreditEntries.getTaxAccountEntry());
		}

		return entries;
	}

	@Override
	public List<Account> generateAccounts() {
		List<Account> accounts = new ArrayList<>();

		Pageable pageable = new PageRequest(0, 50);
		Page<Customer> customersPage = null;
		do {
			customersPage = customerService.findAll(pageable);
			for (Customer customer : customersPage.getContent()) {
				accounts.add(new Account(customerService.getCustomerAccount(customer), StringUtils
						.stripAccents(customer.getFirstName() + " " + customer.getLastName()).toUpperCase()));
			}

			pageable = pageable.next();
		} while (customersPage.hasNext());

		return accounts;
	}

	@Override
	public List<Entry> generateSaleJournalEntries(Date startDate, Date endDate) {
		Page<Invoice> invoicePage = null;
		List<Entry> entries = new ArrayList<>();
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "paidDate");

		do {
			invoicePage = this.invoiceService.findByPaidDateBetweenAndStatus(startDate, endDate,
					new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null), pageRequest);

			for (Invoice invoice : invoicePage.getContent()) {
				try {
					entries.addAll(
							convertInvoiceEntriesToList(transactionEntriesService.generateInvoiceEntries(invoice)));
				} catch (RuntimeException runtimeEx) {
					LOGGER.error("Error while processing invoice " + invoice.getId() + ".");

					throw runtimeEx;
				}
			}

			pageRequest = pageRequest.next();
		} while (invoicePage != null && invoicePage.getContent().size() != 0);

		Page<CustomerCredit> customerCreditPage = null;
		pageRequest = new PageRequest(0, 100, Direction.ASC, "completeDate");
		do {
			customerCreditPage = this.customerCreditService.findByCompleteDateBetweenAndStatus(startDate, endDate,
					CustomerCredit.Status.COMPLETED, pageRequest);

			for (CustomerCredit customerCredit : customerCreditPage.getContent()) {
				entries.addAll(convertCustomerCreditEntriesToList(
						transactionEntriesService.generateCustomerCreditEntries(customerCredit)));
			}

			pageRequest = pageRequest.next();
		} while (customerCreditPage != null && customerCreditPage.getContent().size() != 0);

		Collections.sort(entries, this.entryComparator);

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
						double paymentAmount = payment.getPaymentMethod().getType().equals(PaymentMethod.Types.CASH.toString())
								? payment.getAmount() - invoice.getAmountReturned() : payment.getAmount();

						// Generates a debit entry for the sales account.
						entries.add(new Entry(invoice.getPaidDate(), "op",
								payment.getPaymentMethod().getAccountingCode(), "F" + invoice.getNumber(),
								"REGLEMENT " + this.getCustomerName(invoice, 14), 0d, paymentAmount, "E"));

						// Generates a credit entry for the customer account.
						entries.add(new Entry(invoice.getPaidDate(), "op",
								customerService.getCustomerAccount(invoice.getCustomer()), "F" + invoice.getNumber(),
								StringUtils.stripAccents("REGLEMENT " + this.getCustomerName(invoice, 16)),
								paymentAmount, 0d, "E"));
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
							customerCredit.getPaymentMethod().getAccountingCode(), "A" + customerCredit.getNumber(),
							StringUtils.stripAccents("REMB." + this.getCustomerName(customerCredit, 16)),
							customerCredit.getTotalAmount(), 0d, "E"));

					// Generates a debit entry for the customer account.
					entries.add(new Entry(customerCredit.getCompleteDate(), "op",
							customerService.getCustomerAccount(customerCredit.getCustomer()),
							"A" + customerCredit.getNumber(),
							StringUtils.stripAccents("REMB. " + this.getCustomerName(customerCredit, 16)), 0d,
							customerCredit.getTotalAmount(), "E"));
				}
			}

			pageRequest = pageRequest.next();
		} while (customerCreditPage != null && customerCreditPage.getContent().size() != 0);

		Collections.sort(entries, this.entryComparator);

		return entries;
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
