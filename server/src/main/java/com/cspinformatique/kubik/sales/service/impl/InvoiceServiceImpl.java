package com.cspinformatique.kubik.sales.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.sales.model.CashRegisterSession;
import com.cspinformatique.kubik.sales.model.Invoice;
import com.cspinformatique.kubik.sales.model.InvoiceDetail;
import com.cspinformatique.kubik.sales.model.InvoiceStatus;
import com.cspinformatique.kubik.sales.model.InvoiceStatus.Types;
import com.cspinformatique.kubik.sales.model.InvoiceTaxAmount;
import com.cspinformatique.kubik.sales.model.Payment;
import com.cspinformatique.kubik.sales.repository.InvoiceRepository;
import com.cspinformatique.kubik.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.sales.service.CashRegisterSessionService;
import com.cspinformatique.kubik.sales.service.DailyReportService;
import com.cspinformatique.kubik.sales.service.InvoiceService;
import com.cspinformatique.kubik.warehouse.service.ProductInventoryService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	private CashRegisterSessionService cashRegisterSessionService;

	@Autowired
	private DailyReportService dailyReportService;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private InvoiceStatusRepository invoiceStatusRepository;

	@Autowired
	private ProductInventoryService productInventoryService;

	@Autowired
	private ProductService productService;

	private DateFormat dateFormat;

	public InvoiceServiceImpl() {
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
	}

	private void addInventory(Invoice invoice) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			this.productInventoryService.addInventory(detail.getProduct(),
					detail.getQuantity());
		}
	}

	private void calculateInvoiceAmounts(Invoice invoice) {
		HashMap<Double, InvoiceTaxAmount> totalTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

		double totalAmount = 0d;
		double totalTaxLessAmount = 0d;
		double totalTaxAmount = 0d;

		if (invoice.getDetails() != null) {
			for (InvoiceDetail detail : invoice.getDetails()) {
				Product product = productService.findOne(detail.getProduct()
						.getId());
				double quantity = detail.getQuantity();

				// Builds the tax rates / amounts map.
				Map<Double, InvoiceTaxAmount> detailTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();
				if (product.getTvaRate1() != null
						&& product.getPriceTaxOut1() != null
						&& product.getTvaRate1() != 0d) {
					InvoiceTaxAmount invoiceTaxAmount = detailTaxesAmounts
							.get(product.getTvaRate1());

					if (invoiceTaxAmount == null) {
						invoiceTaxAmount = new InvoiceTaxAmount(null,
								product.getTvaRate1(), 0d);
					}

					invoiceTaxAmount.setTaxAmount(invoiceTaxAmount
							.getTaxAmount()
							+ ((product.getPriceTaxIn() - product
									.getPriceTaxOut1()) * quantity));

					detailTaxesAmounts.put(product.getTvaRate1(),
							invoiceTaxAmount);
				}

				if (product.getTvaRate2() != null
						&& product.getPriceTaxOut2() != null
						&& product.getTvaRate2() != 0d) {
					InvoiceTaxAmount invoiceTaxAmount = detailTaxesAmounts
							.get(product.getTvaRate2());

					if (invoiceTaxAmount == null) {
						invoiceTaxAmount = new InvoiceTaxAmount(null,
								product.getTvaRate2(), 0d);
					}

					invoiceTaxAmount.setTaxAmount(invoiceTaxAmount
							.getTaxAmount()
							+ ((product.getPriceTaxIn() - product
									.getPriceTaxOut2()) * quantity));

					detailTaxesAmounts.put(product.getTvaRate2(),
							invoiceTaxAmount);
				}

				if (product.getTvaRate3() != null
						&& product.getPriceTaxOut3() != null
						&& product.getTvaRate3() != 0d) {
					InvoiceTaxAmount invoiceTaxAmount = detailTaxesAmounts
							.get(product.getTvaRate3());

					if (invoiceTaxAmount == null) {
						invoiceTaxAmount = new InvoiceTaxAmount(null,
								product.getTvaRate3(), 0d);
					}

					invoiceTaxAmount.setTaxAmount(invoiceTaxAmount
							.getTaxAmount()
							+ ((product.getPriceTaxIn() - product
									.getPriceTaxOut3()) * quantity));

					detailTaxesAmounts.put(product.getTvaRate3(),
							invoiceTaxAmount);
				}

				// Calculates invoice details amounts
				detail.setUnitPrice(product.getPriceTaxIn());
				detail.setTotalAmount(detail.getUnitPrice() * quantity);
				detail.setTaxesAmounts(detailTaxesAmounts);

				double detailTotalTaxAmount = 0d;
				double detailTaxLessAmount = product.getPriceTaxIn() * quantity;
				for (InvoiceTaxAmount taxAmount : detail.getTaxesAmounts()
						.values()) {
					double amount = taxAmount.getTaxAmount();
					double taxRate = taxAmount.getTaxRate();

					detailTaxLessAmount -= amount;
					detailTotalTaxAmount += amount;

					InvoiceTaxAmount invoiceTaxAmount = totalTaxesAmounts
							.get(taxRate);
					if (invoiceTaxAmount == null) {
						invoiceTaxAmount = new InvoiceTaxAmount(null, taxRate,
								0d);
					}

					invoiceTaxAmount.setTaxAmount(invoiceTaxAmount
							.getTaxAmount() + detailTotalTaxAmount);

					totalTaxesAmounts.put(taxRate, invoiceTaxAmount);
				}

				detail.setTotalTaxAmount(detailTotalTaxAmount);
				detail.setTotalTaxLessAmount(detailTaxLessAmount);

				// Increment invoice totals amount.
				totalAmount += detail.getTotalAmount();
				totalTaxAmount += detailTotalTaxAmount;
				totalTaxLessAmount += detailTaxLessAmount;
			}
		}

		invoice.setTotalAmount(Precision.round(totalAmount, 2));
		invoice.setTotalTaxAmount(Precision.round(totalTaxAmount, 2));
		invoice.setTotalTaxLessAmount(Precision.round(totalTaxLessAmount, 2));
		invoice.setTaxesAmounts(totalTaxesAmounts);

		// Calculate cash to return.
		double amountPaid = 0d;
		if (invoice.getPayments() != null) {
			for (Payment payment : invoice.getPayments()) {
				amountPaid += payment.getAmount();
			}
		}

		invoice.setAmountPaid(amountPaid);
		invoice.setAmountReturned(invoice.getAmountPaid()
				- invoice.getTotalAmount());
	}

	@Override
	public Iterable<Invoice> findInvoiceByCashRegisterSessionAndInDraft(
			CashRegisterSession session) {
		List<Invoice> invoices = this.invoiceRepository
				.findByCashRegisterSessionAndStatus(session,
						invoiceStatusRepository.findOne(Types.DRAFT.name()));

		if (invoices.size() == 0) {
			invoices.add(this.generateNewInvoice(session));
		}

		return invoices;

	}

	@Override
	public Page<Invoice> findAll(Pageable pageable) {
		return this.invoiceRepository.findAll(pageable);
	}

	@Override
	public Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable) {
		return this.invoiceRepository.findByStatus(status, pageable);
	}

	@Override
	public List<Invoice> findByPaidDate(Date paidDate) {
		return this.invoiceRepository.findByPaidDateBetweenAndStatus(paidDate,
				LocalDate.fromDateFields(paidDate).plusDays(1).toDate(),
				new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null));
	}

	@Override
	public Invoice findFirstPaidInvoice() {
		Page<Invoice> page = this.invoiceRepository.findByStatus(
				new InvoiceStatus(Types.PAID.toString(), null),
				new PageRequest(0, 1, Direction.ASC, "paidDate"));

		if (page.getContent().size() == 0) {
			return null;
		}

		return page.getContent().get(0);
	}

	@Override
	public Invoice findOne(int id) {
		return this.invoiceRepository.findOne(id);
	}

	@Override
	public Invoice generateNewInvoice(CashRegisterSession session) {
		return this.save(new Invoice(null, null, null, invoiceStatusRepository
				.findOne(Types.DRAFT.name()), null, new Date(), null, null,
				null, null, 0d, 0d, new HashMap<Double, InvoiceTaxAmount>(),
				0d, 0d, new ArrayList<Payment>(), 0d, 0d, session,
				new ArrayList<InvoiceDetail>()));
	}

	private void generateInvoiceNumber(Invoice invoice) {
		String dateString = dateFormat.format(new Date());

		Page<Invoice> page = this.findByStatus(
				this.invoiceStatusRepository.findOne(Types.PAID.name()),
				new PageRequest(0, 1, Direction.DESC, "paidDate"));

		long number = Long.valueOf(dateString + "0001");
		if (page.getContent().size() > 0) {
			long lastNumber = page.getContent().get(0).getNumber();
			String lastDateString = String.valueOf(lastNumber).substring(0, 8);

			if (lastDateString.equals(dateString)) {
				number = lastNumber + 1;
			}
		}

		invoice.setNumber(number);
	}

	private void removeInventory(Invoice invoice) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			this.productInventoryService.removeInventory(detail.getProduct(),
					detail.getQuantity());
		}
	}

	@Override
	public Invoice save(Invoice invoice) {
		if (invoice.getId() != null) {
			Invoice oldInvoice = this.findOne(invoice.getId());

			String status = invoice.getStatus().getType();

			if (oldInvoice != null) {
				if (status.equals(Types.CANCELED.name())
						&& !oldInvoice.getStatus().getType()
								.equals(Types.CANCELED.name())) {
					invoice.setCancelDate(new Date());
				}

				if (status.equals(Types.PAID.name())
						&& !oldInvoice.getStatus().getType()
								.equals(Types.PAID.name())) {
					invoice.setPaidDate(new Date());

					this.generateInvoiceNumber(invoice);

					// Update inventory.
					this.removeInventory(invoice);
				}

				if (status.equals(Types.REFUND.name())
						&& !oldInvoice.getStatus().getType()
								.equals(Types.REFUND.name())) {
					invoice.setRefundDate(new Date());

					this.addInventory(invoice);
				}
			}
		}

		// Calculate invoice amounts.
		this.calculateInvoiceAmounts(invoice);

		Invoice newInvoice = this.invoiceRepository.save(invoice);

		// Recalculate daily report.
		if (invoice.getPaidDate() != null) {
			this.dailyReportService.generateDailyReport(invoice.getPaidDate());
		}

		return newInvoice;
	}
}
