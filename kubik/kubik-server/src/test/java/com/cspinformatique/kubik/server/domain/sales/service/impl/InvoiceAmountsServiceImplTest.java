package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.cspinformatique.kubik.server.domain.sales.service.InvoiceAmountsService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;

public class InvoiceAmountsServiceImplTest {
	private InvoiceAmountsService service = new InvoiceAmountsServiceImpl();

	@Test
	public void testCalculateProductUnitPrice() {
		InvoiceDetail invoiceDetail = new InvoiceDetail();
		invoiceDetail.setUnitPrice(15d);
		invoiceDetail.setRebatePercent(10d);
		invoiceDetail.setQuantity(2d);

		invoiceDetail.setInvoice(new Invoice());

		invoiceDetail.setProduct(new Product());
		invoiceDetail.getProduct().setTvaRate1(5.5d);

		// Calculate amounts.
		service.calculateDetailAmounts(invoiceDetail);

		// Asserts Detail Total Amount.
		Assert.assertThat(
				BigDecimal.valueOf(invoiceDetail.getTotalAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
				Matchers.is(27.00d));

		// Assets Product Unit Tax Less Price
		Assert.assertThat(
				BigDecimal.valueOf(invoiceDetail.getUnitPriceTaxLess()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
				Matchers.is(14.22d));

		// Assets Detail Tax Less Total Price
		Assert.assertThat(BigDecimal.valueOf(invoiceDetail.getTotalTaxLessAmount()).setScale(2, RoundingMode.HALF_UP)
				.doubleValue(), Matchers.is(25.59d));

		// Asserts Rebate Amount.
		Assert.assertThat(BigDecimal.valueOf(invoiceDetail.getRebate()).doubleValue(), Matchers.is(3.00d));
	}
}
