package com.cspinformatique.kubik.domain.sales.service.impl;

import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cspinformatique.kubik.KubikTest;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.model.sales.Invoice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { KubikTest.class }, loader = SpringApplicationContextLoader.class)
public class InvoiceServiceImplIT {
	@Autowired
	InvoiceService invoiceService;

	@Test
	@Transactional
	public void calculateInvoiceAmountsTest() {
		Invoice invoice = invoiceService.findOne(2961);

		invoice.getTaxesAmounts().clear();
		invoice.setTotalTaxAmount(0d);
		invoice.setTotalTaxLessAmount(0d);

		invoiceService.calculateInvoiceTaxes(invoice);

		Assert.assertTrue("Total amount for invoice 1983 was " + invoice.getTotalAmount() + " expected 44.45.",
				invoice.getTotalAmount() == 44.45d);
		Assert.assertTrue("Total tax amount for invoice 286 should equals 2.32.",
				Precision.round(invoice.getTotalTaxAmount(), 2) == 2.32d);
		Assert.assertTrue("Total tax less amount for invoice 286 should equals 42.13.",
				Precision.round(invoice.getTotalTaxLessAmount(), 2) == 42.13d);

	}

	@Test
	@Transactional
	public void validateInvoicesRebates() {
		Invoice invoice = invoiceService.findOne(2948);

		Assert.assertTrue(
				"Rebate amount " + invoice.getRebateAmount() + " doesn not match " + invoice.getRebatePercent()
						+ " % rebate for a total amount of " + invoice.getTotalAmount() + ".",
				invoice.getRebateAmount() == Precision
						.round((invoice.getTotalAmount() * (invoice.getRebatePercent() / 100)), 2));
	}
}
