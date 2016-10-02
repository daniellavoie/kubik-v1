package com.cspinformatique.kubik.server.domain.sales.service.impl;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.sales.service.InvoiceAmountsService;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;

@Service
public class InvoiceAmountsServiceImpl implements InvoiceAmountsService {

	@Override
	public void calculateDetailAmounts(InvoiceDetail invoiceDetail) {
		invoiceDetail.setTotalAmount(calculateDetailTotalAmount(invoiceDetail));
		invoiceDetail.setRebate(calculateRebateAmount(invoiceDetail));
		invoiceDetail.setTotalTaxLessAmount(calculateDetailTaxLessTotal(invoiceDetail));
		invoiceDetail.setUnitPriceTaxLess(calculateProductUnitTaxLessPrice(invoiceDetail));
	}

	/**
	 * Calculates the total tax less amounts for the given detail.
	 * 
	 * Formula applied : tax-in * ( 1 - unit rebate-percent / 100) * quantity /
	 * (1 + tax percent / 100)
	 * 
	 * @param invoiceDetail
	 *            invoice detail on which the total tax less amount should be
	 *            calculated
	 * @return
	 */
	private double calculateDetailTaxLessTotal(InvoiceDetail invoiceDetail) {
		return calculateDetailTotalAmount(invoiceDetail) / (1 + invoiceDetail.getProduct().getTvaRate1() / 100);
	}

	private double calculateDetailTotalAmount(InvoiceDetail invoiceDetail) {
		double rebatePercent = calculateDetailUnitRebatePercent(invoiceDetail);

		return invoiceDetail.getUnitPrice() * (1 - rebatePercent / 100) * invoiceDetail.getQuantity();
	}

	private double calculateDetailUnitRebatePercent(InvoiceDetail invoiceDetail) {
		double rebatePercent = 0d;
		if (invoiceDetail.getInvoice().getRebatePercent() != null)
			rebatePercent = invoiceDetail.getInvoice().getRebatePercent();

		if (invoiceDetail.getRebatePercent() != null && rebatePercent < invoiceDetail.getRebatePercent())
			rebatePercent = invoiceDetail.getRebatePercent();

		return rebatePercent;
	}

	/**
	 * Formula applied : tax-in / (1 + tax percent / 100)
	 * 
	 * @param invoiceDetail
	 *            Invoice detail on which the unit price should be calculated
	 */
	private double calculateProductUnitTaxLessPrice(InvoiceDetail invoiceDetail) {
		return invoiceDetail.getUnitPrice().doubleValue() / (1 + invoiceDetail.getProduct().getTvaRate1() / 100);
	}

	private double calculateRebateAmount(InvoiceDetail invoiceDetail) {
		double rebatePercent = calculateDetailUnitRebatePercent(invoiceDetail);

		return invoiceDetail.getUnitPrice() * (rebatePercent / 100) * invoiceDetail.getQuantity();
	}

}
