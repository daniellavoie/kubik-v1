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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = KubikServerTest.class)
public class InvoiceServiceImplIT {
	@Resource
	private InvoiceService invoiceService;

	@Test
	@Transactional
	public void calculateInvoiceAmountsTest() {
		int INVOICE_ID = 11704;
		Invoice invoice = invoiceService.findOne(INVOICE_ID);

		invoice.getTaxesAmounts().clear();
		invoice.setTotalTaxAmount(0d);
		invoice.setTotalTaxLessAmount(0d);

		invoice.getDetails().stream().filter(detail -> detail.getProduct().getName().contains("AGENDA"))
				.findAny().get().setRebatePercent(20d);

		invoiceService.save(invoice);
		
		Assert.assertTrue(
				"Total amount for invoice " + INVOICE_ID + " was " + invoice.getTotalAmount() + " but expected 52.36.",
				invoice.getTotalAmount() == 52.36d);
		Assert.assertTrue("Total tax amount for invoice 286 should equals 6.15.",
				Precision.round(invoice.getTotalTaxAmount(), 2) == 6.15d);
		Assert.assertTrue("Total tax less amount for invoice 286 should equals 46.21.",
				Precision.round(invoice.getTotalTaxLessAmount(), 2) == 46.21d);

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
