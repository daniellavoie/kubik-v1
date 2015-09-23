package com.cspinformatique.kubik.domain.sales.service.impl;

import java.math.BigDecimal;

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
import com.cspinformatique.kubik.model.sales.InvoiceDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { KubikTest.class }, loader = SpringApplicationContextLoader.class)
public class InvoiceServiceImplIT {
	@Autowired
	InvoiceService invoiceService;

	@Test
	@Transactional
	public void calculateInvoiceAmountsTest() {
		Invoice invoice = invoiceService.findOne(286);

		invoice.setTaxesAmounts(null);
		invoice.setTotalAmount(0d);
		invoice.setTotalTaxAmount(0d);
		invoice.setTotalTaxLessAmount(0d);

		BigDecimal totalTaxAmount = new BigDecimal(0d);
		BigDecimal totalTaxLessAmount = new BigDecimal(0d);
		for (InvoiceDetail detail : invoice.getDetails()) {
			totalTaxAmount = totalTaxAmount.add(new BigDecimal(detail.getTotalTaxAmount()));
			totalTaxLessAmount = totalTaxLessAmount.add(new BigDecimal(detail.getTotalTaxAmount()));
		}

		invoiceService.calculateInvoiceTaxes(invoice);

		Assert.assertTrue("Total amount for invoice 286 should equals 29.", invoice.getTotalAmount() == 29d);
		Assert.assertTrue("Total tax amount for invoice 286 should equals 1.6.",
				Precision.round(totalTaxAmount.doubleValue(), 2) == 1.6d);
		Assert.assertTrue("Total tax less amount for invoice 286 should equals .",
				Precision.round(totalTaxLessAmount.doubleValue(), 2) == 1.6d);
	}
}
