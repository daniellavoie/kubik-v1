package com.cspinformatique.kubik.server.domain.accounting.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.accounting.model.CustomerCreditDebitEntries;
import com.cspinformatique.kubik.server.domain.accounting.model.CustomerCreditEntries;
import com.cspinformatique.kubik.server.domain.accounting.model.Entry;
import com.cspinformatique.kubik.server.domain.accounting.model.InvoiceCreditEntries;
import com.cspinformatique.kubik.server.domain.accounting.model.InvoiceEntries;
import com.cspinformatique.kubik.server.domain.accounting.service.TransactionEntriesService;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceTaxAmount;

@Service
public class TransactionEntriesServiceImpl implements TransactionEntriesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEntriesServiceImpl.class);

	private static final String ANONYMUS_CLIENT = "ANONYME";
	private static final String PRODUCT_ACCOUNT_PREFIX = "7070";
	private static final String TAX_ACCOUNT_PREFIX = "4457";

	@Resource
	private CustomerService customerService;

	private DecimalFormat decimalFormat;
	private DecimalFormatSymbols productAccountdecimalFormatSymbols;

	public TransactionEntriesServiceImpl() {
		this.decimalFormat = new DecimalFormat("0.0");
		this.productAccountdecimalFormatSymbols = new DecimalFormatSymbols();
	}

	@Override
	public CustomerCreditEntries generateCustomerCreditEntries(CustomerCredit customerCredit) {
		return new CustomerCreditEntries(generateCustomerCreditDebitsEntries(customerCredit),
				generateCustomerCreditCreditEntry(customerCredit));
	}

	private List<CustomerCreditDebitEntries> generateCustomerCreditDebitsEntries(CustomerCredit customerCredit) {
		List<CustomerCreditDebitEntries> customerCreditDebitsEntries = new ArrayList<>();

		for (InvoiceTaxAmount invoiceTaxAmount : customerCredit.getTaxesAmounts().values()) {
			// Build tax account entry
			Entry taxAccountEntry = new Entry(customerCredit.getCompleteDate(), "vt",
					getProductTaxAccount(invoiceTaxAmount.getTaxRate()), "A" + customerCredit.getNumber(),
					StringUtils.stripAccents("RETOUR " + this.getCustomerName(customerCredit, 14)), 0d,
					invoiceTaxAmount.getTaxAmount(), "E");

			// Build product account entry
			Entry productAccountEntry = new Entry(customerCredit.getCompleteDate(), "vt",
					getProductAccount(invoiceTaxAmount.getTaxRate()), "A" + customerCredit.getNumber(),
					StringUtils.stripAccents("RETOUR " + this.getCustomerName(customerCredit, 14)), 0d,
					invoiceTaxAmount.getTaxableAmount(), "E");

			// Build customer credit debit entries
			CustomerCreditDebitEntries customerCreditDebitEntries = new CustomerCreditDebitEntries(
					invoiceTaxAmount.getTaxRate(), taxAccountEntry, productAccountEntry);

			customerCreditDebitsEntries.add(customerCreditDebitEntries);
		}

		return customerCreditDebitsEntries;
	}

	private Entry generateCustomerCreditCreditEntry(CustomerCredit customerCredit) {
		return new Entry(customerCredit.getCompleteDate(), "vt",
				customerService.getCustomerAccount(customerCredit.getCustomer()), "A" + customerCredit.getNumber(),
				StringUtils.stripAccents("RETOUR " + this.getCustomerName(customerCredit, 14)),
				customerCredit.getTotalAmount(), 0d, "E");
	}

	@Override
	public InvoiceEntries generateInvoiceEntries(Invoice invoice) {
		InvoiceEntries invoiceEntries = new InvoiceEntries(generateInvoiceDebitEntry(invoice),
				generateInvoiceCreditsEntries(invoice));

		BigDecimal credit = new BigDecimal(0d);
		BigDecimal debit = new BigDecimal(invoiceEntries.getDebitEntry().getDebit());

		for (InvoiceCreditEntries invoiceCreditEntries : invoiceEntries.getCreditEntries()) {
			credit = credit.add(new BigDecimal(invoiceCreditEntries.getTaxAccountEntry().getCredit()));
			credit = credit.add(new BigDecimal(invoiceCreditEntries.getProductAccountEntry().getCredit()));
		}

		double totalCredit = Precision.round(credit.doubleValue(), 2);
		double totalDebit = Precision.round(debit.doubleValue(), 2);
		if (totalCredit != totalDebit) {
			String header = "Invoice " + invoice.getNumber() + " : ";
			LOGGER.warn("Invoice " + invoice.getNumber() + " does not balance. Credit : " + totalCredit + " | Debit : "
					+ totalDebit + ".");

			LOGGER.warn(header + " amount : " + invoice.getTotalAmount());
			LOGGER.warn(header + " tax : " + invoice.getTotalTaxAmount());
			LOGGER.warn(header + " rebate : " + invoice.getRebateAmount());
			LOGGER.warn(header + " debit : " + invoiceEntries.getDebitEntry().getDebit());
			for (InvoiceCreditEntries invoiceCreditEntries : invoiceEntries.getCreditEntries()) {
				LOGGER.warn(header + " credit " + invoiceCreditEntries.getTaxRate() + " tax account : "
						+ invoiceCreditEntries.getTaxAccountEntry().getCredit());
				LOGGER.warn(header + " credit " + invoiceCreditEntries.getTaxRate() + " product account : "
						+ invoiceCreditEntries.getProductAccountEntry().getCredit());

			}
		}

		return invoiceEntries;
	}

	private List<InvoiceCreditEntries> generateInvoiceCreditsEntries(Invoice invoice) {
		List<InvoiceCreditEntries> invoiceCreditsEntries = new ArrayList<>();

		for (InvoiceTaxAmount invoiceTaxAmount : invoice.getTaxesAmounts().values()) {
			// Build tax account entry
			Entry taxAccountEntry = new Entry(invoice.getPaidDate(), "vt",
					getProductTaxAccount(invoiceTaxAmount.getTaxRate()), "F" + invoice.getNumber(),
					StringUtils.stripAccents("VENTE " + this.getCustomerName(invoice, 16)),
					invoiceTaxAmount.getTaxAmount(), 0d, "E");

			// Build product account entry
			Entry productAccountEntry = new Entry(invoice.getPaidDate(), "vt",
					getProductAccount(invoiceTaxAmount.getTaxRate()), "F" + invoice.getNumber(),
					StringUtils.stripAccents("VENTE " + this.getCustomerName(invoice, 16)),
					invoiceTaxAmount.getTaxableAmount(), 0d, "E");

			// Build InvoiceCreditEntries
			InvoiceCreditEntries invoiceCreditEntries = new InvoiceCreditEntries(invoiceTaxAmount.getTaxRate(),
					taxAccountEntry, productAccountEntry);

			invoiceCreditsEntries.add(invoiceCreditEntries);
		}

		return invoiceCreditsEntries;
	}

	private Entry generateInvoiceDebitEntry(Invoice invoice) {
		String customerName = this.getCustomerName(invoice, 16);

		Entry customerDebitEntry = new Entry(invoice.getPaidDate(), "vt",
				customerService.getCustomerAccount(invoice.getCustomer()), "F" + invoice.getNumber(),
				StringUtils.stripAccents("VENTE " + customerName), 0d, invoice.getTotalAmount(), "E");

		LOGGER.debug("Generating a new debit entry for invoice " + invoice.getId() + " to customer account "
				+ customerDebitEntry.getAccount() + ".");

		return customerDebitEntry;
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

	private String getProductAccount(double taxRate) {
		String productAccountSufix = decimalFormat.format(taxRate)
				.replace(productAccountdecimalFormatSymbols.getDecimalSeparator() + "", "");

		productAccountSufix = productAccountSufix.substring(productAccountSufix.length() - 2,
				productAccountSufix.length());

		return PRODUCT_ACCOUNT_PREFIX + productAccountSufix;
	}

	private String getProductTaxAccount(double taxRate) {
		return TAX_ACCOUNT_PREFIX + decimalFormat.format(taxRate).replace(".", "").substring(0, 2);
	}
}
