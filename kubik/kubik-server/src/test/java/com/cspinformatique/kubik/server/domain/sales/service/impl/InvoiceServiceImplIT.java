package com.cspinformatique.kubik.server.domain.sales.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cspinformatique.kubik.server.KubikServerTest;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.model.sales.Invoice;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes = KubikServerTest.class )
public class InvoiceServiceImplIT {
	@Resource
	InvoiceService invoiceService;

	@Test
	@Transactional
	public void calculateInvoiceAmountsTest() {
		Invoice invoice = invoiceService.findOne(2961);

		invoice.getTaxesAmounts().clear();
		invoice.setTotalTaxAmount(0d);
		invoice.setTotalTaxLessAmount(0d);

		invoiceService.calculateInvoiceTaxes(invoice);

		Assert.assertTrue("Total amount for invoice 1983 was " + invoice.getTotalAmount() + " but expected 44.45.",
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
