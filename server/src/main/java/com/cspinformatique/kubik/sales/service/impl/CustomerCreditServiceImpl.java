package com.cspinformatique.kubik.sales.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.sales.model.CustomerCredit;
import com.cspinformatique.kubik.sales.model.CustomerCreditDetail;
import com.cspinformatique.kubik.sales.model.Invoice;
import com.cspinformatique.kubik.sales.model.InvoiceDetail;
import com.cspinformatique.kubik.sales.model.InvoiceTaxAmount;
import com.cspinformatique.kubik.sales.repository.CustomerCreditRepository;
import com.cspinformatique.kubik.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.sales.service.InvoiceService;

@Service
public class CustomerCreditServiceImpl implements CustomerCreditService {
	@Autowired
	private CustomerCreditRepository customerCreditRepository;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private ProductService productService;

	private void calculateAmounts(CustomerCredit customerCredit) {
		HashMap<Double, InvoiceTaxAmount> totalTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

		double totalAmount = 0d;
		double totalTaxLessAmount = 0d;
		double totalTaxAmount = 0d;

		Invoice invoice = this.invoiceService.findOne(customerCredit
				.getInvoice().getId());

		if (invoice == null) {
			throw new RuntimeException("Invoice "
					+ customerCredit.getInvoice().getId()
					+ " could not be found.");
		}

		if (customerCredit.getDetails() != null) {
			for (CustomerCreditDetail detail : customerCredit.getDetails()) {
				Product product = productService.findOne(detail.getProduct()
						.getId());

				double quantity = detail.getQuantity();

				InvoiceDetail invoiceDetail = this.findInvoiceDetail(invoice,
						detail);

				if (invoiceDetail == null) {
					throw new RuntimeException(
							"Invoice detail could not be found for product "
									+ detail.getProduct().getId()
									+ " in invoice " + invoice.getId() + ".");
				}

				// Builds the tax rates / amounts map.
				Map<Double, InvoiceTaxAmount> detailTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

				for (InvoiceTaxAmount invoiceTaxAmount : invoiceDetail
						.getTaxesAmounts().values()) {
					InvoiceTaxAmount creditTaxAmount = detailTaxesAmounts
							.get(invoiceTaxAmount.getTaxRate());

					if (creditTaxAmount == null) {
						invoiceTaxAmount = new InvoiceTaxAmount(null,
								invoiceTaxAmount.getTaxRate(), 0d);
					}

					creditTaxAmount.setTaxAmount(creditTaxAmount.getTaxAmount()
							+ Precision.round(
									invoiceTaxAmount.getTaxAmount()
											/ invoiceDetail.getQuantity()
											* detail.getQuantity(), 2));

					detailTaxesAmounts.put(creditTaxAmount.getTaxRate(),
							creditTaxAmount);
				}

				// Calculates customer credit details amounts
				detail.setUnitPrice(invoiceDetail.getUnitPrice());
				detail.setTotalAmount(invoiceDetail.getUnitPrice() * quantity);

				if (detail.getTaxesAmounts() != null) {
					detail.getTaxesAmounts().clear();
					detail.getTaxesAmounts().putAll(detailTaxesAmounts);
				} else {
					detail.setTaxesAmounts(detailTaxesAmounts);
				}

				double detailTotalTaxAmount = 0d;
				double detailTaxLessAmount = product.getPriceTaxIn() * quantity;
				for (InvoiceTaxAmount taxAmount : detail.getTaxesAmounts()
						.values()) {
					double amount = taxAmount.getTaxAmount();
					double taxRate = taxAmount.getTaxRate();

					detailTaxLessAmount -= amount;
					detailTotalTaxAmount += amount;

					InvoiceTaxAmount creditTaxAmount = totalTaxesAmounts
							.get(taxRate);
					if (creditTaxAmount == null) {
						creditTaxAmount = new InvoiceTaxAmount(null, taxRate,
								0d);
					}

					creditTaxAmount.setTaxAmount(creditTaxAmount.getTaxAmount()
							+ detailTotalTaxAmount);

					totalTaxesAmounts.put(taxRate, creditTaxAmount);
				}

				detail.setTotalTaxAmount(detailTotalTaxAmount);
				detail.setTotalTaxLessAmount(detailTaxLessAmount);

				// Increment customer credit totals amount.
				totalAmount += detail.getTotalAmount();
				totalTaxAmount += detailTotalTaxAmount;
				totalTaxLessAmount += detailTaxLessAmount;
			}
		}

		customerCredit.setTotalAmount(Precision.round(totalAmount, 2));
		customerCredit.setTotalTaxAmount(Precision.round(totalTaxAmount, 2));
		customerCredit.setTotalTaxLessAmount(Precision.round(
				totalTaxLessAmount, 2));

		if (customerCredit.getTaxesAmounts() != null) {
			customerCredit.getTaxesAmounts().clear();
			customerCredit.getTaxesAmounts().putAll(totalTaxesAmounts);
		} else {
			customerCredit.setTaxesAmounts(totalTaxesAmounts);
		}
	}

	@Override
	public Page<CustomerCredit> findAll(Pageable pageable) {
		return this.customerCreditRepository.findAll(pageable);
	}
	
	@Override
	public CustomerCredit findOne(int id){
		return this.customerCreditRepository.findOne(id);
	}

	private InvoiceDetail findInvoiceDetail(Invoice invoice,
			CustomerCreditDetail customerCreditDetail) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			if (detail.getProduct().getId() == customerCreditDetail
					.getProduct().getId()) {
				return detail;
			}
		}

		return null;
	}

	@Override
	public CustomerCredit save(CustomerCredit customerCredit) {
		this.calculateAmounts(customerCredit);
		
		return this.customerCreditRepository.save(customerCredit);
	}
}
