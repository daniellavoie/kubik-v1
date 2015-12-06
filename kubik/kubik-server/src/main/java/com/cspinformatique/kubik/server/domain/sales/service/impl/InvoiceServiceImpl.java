package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.purchase.service.RestockService;
import com.cspinformatique.kubik.server.domain.sales.repository.InvoiceRepository;
import com.cspinformatique.kubik.server.domain.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.server.domain.sales.service.DailyReportService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.CashRegisterSession;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.server.model.sales.InvoiceTaxAmount;
import com.cspinformatique.kubik.server.model.sales.Payment;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus.Types;

@Service
public class InvoiceServiceImpl implements InvoiceService {

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

	@Autowired
	private RestockService restockService;

	private void calculateInvoiceAmounts(Invoice invoice) {
		double totalAmount = 0d;
		double totalRebateAmount = 0d;

		for (InvoiceDetail detail : invoice.getDetails()) {
			double quantity = detail.getQuantity();

			Product product = productService.findOne(detail.getProduct().getId());

			// Calculate rebate amount for detail.
			double rebateAmount = 0d;
			if (invoice.getRebatePercent() != null && invoice.getRebatePercent().doubleValue() != 0d) {
				rebateAmount = product.getPriceTaxIn() * (invoice.getRebatePercent() / 100);
			}

			// Increment total rebate amount.
			totalRebateAmount += rebateAmount;

			// Calculates customer credit details amounts
			detail.setUnitPrice(product.getPriceTaxIn());
			detail.setTotalAmount(detail.getUnitPrice() * quantity);

			// Increment customer credit totals amount.
			totalAmount += detail.getTotalAmount();
		}

		totalRebateAmount = Precision.round(totalRebateAmount, 2);
		invoice.setRebateAmount(totalRebateAmount);
		invoice.setTotalAmount(Precision.round(totalAmount - totalRebateAmount, 2));

		calculateInvoiceTaxes(invoice);

		// Calculate cash to return.
		double amountPaid = 0d;
		if (invoice.getPayments() != null) {
			for (Payment payment : invoice.getPayments()) {
				amountPaid += payment.getAmount();
			}
		}

		invoice.setAmountPaid(amountPaid);
		invoice.setAmountReturned(invoice.getAmountPaid() - invoice.getTotalAmount());
	}

	public void calculateInvoiceRebatePercent(Invoice invoice) {
		if (invoice.getRebateAmount() != 0d) {
			invoice.setRebatePercent(Precision.round((invoice.getRebateAmount() * 100) / invoice.getTotalAmount(), 2));
		}
	}

	@Override
	public void calculateInvoiceTaxes(Invoice invoice) {
		HashMap<Double, InvoiceTaxAmount> totalTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

		double totalTaxableAmount = 0d;
		double totalTaxAmount = 0d;

		for (InvoiceTaxAmount invoiceTaxAmount : invoice.getTaxesAmounts().values()) {
			invoiceTaxAmount.setTaxAmount(0d);
			invoiceTaxAmount.setTaxableAmount(0d);
			invoiceTaxAmount.setTaxedAmount(0d);
		}

		for (InvoiceDetail detail : invoice.getDetails()) {
			Product product = productService.findOne(detail.getProduct().getId());

			double quantity = detail.getQuantity();

			// Calculate rebate amount for detail.
			double rebateAmount = 0d;
			if (invoice.getRebatePercent() != null && invoice.getRebatePercent().doubleValue() != 0d) {
				rebateAmount = detail.getUnitPrice() * invoice.getRebatePercent() / 100;
			}

			double detailTotalAmount = detail.getUnitPrice().doubleValue() * quantity - rebateAmount;

			// Increment taxed amounts.
			InvoiceTaxAmount invoiceTaxAmount = totalTaxesAmounts.get(product.getTvaRate1());
			if (invoiceTaxAmount == null) {
				invoiceTaxAmount = new InvoiceTaxAmount(0, product.getTvaRate1(), 0d, 0d, 0d);
			} else if (invoiceTaxAmount.getTaxedAmount() == null) {
				invoiceTaxAmount.setTaxedAmount(0d);
			}
			invoiceTaxAmount.setTaxedAmount(detailTotalAmount + invoiceTaxAmount.getTaxedAmount());

			totalTaxesAmounts.put(product.getTvaRate1(), invoiceTaxAmount);
		}

		for (InvoiceTaxAmount invoiceTaxAmount : totalTaxesAmounts.values()) {
			invoiceTaxAmount.setTaxAmount(Precision.round((invoiceTaxAmount.getTaxedAmount()
					/ (1 + (invoiceTaxAmount.getTaxRate() / 100)) * (invoiceTaxAmount.getTaxRate() / 100)), 2));
			invoiceTaxAmount.setTaxableAmount(
					Precision.round(invoiceTaxAmount.getTaxedAmount() - invoiceTaxAmount.getTaxAmount(), 2));
			
			totalTaxAmount += invoiceTaxAmount.getTaxAmount();
			totalTaxableAmount += invoiceTaxAmount.getTaxableAmount();
		}

		invoice.setTotalTaxAmount(Precision.round(totalTaxAmount, 2));
		invoice.setTotalTaxLessAmount(Precision.round(totalTaxableAmount, 2));

		if (invoice.getTaxesAmounts() != null) {
			invoice.getTaxesAmounts().clear();
			invoice.getTaxesAmounts().putAll(totalTaxesAmounts);
		} else {
			invoice.setTaxesAmounts(totalTaxesAmounts);
		}
	}

	@Override
	public InvoiceDetail findDetailByInvoiceIdAndProductEan13(int invoiceId, String ean13) {
		return this.invoiceRepository.findDetailByInvoiceIdAndProductEan13(invoiceId, ean13);
	}

	@Override
	public Iterable<Invoice> findInvoiceByCashRegisterSessionAndInDraft(CashRegisterSession session) {
		List<Invoice> invoices = this.invoiceRepository.findByCashRegisterSessionAndStatus(session,
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
	public Invoice findByNumber(String number) {
		return this.invoiceRepository.findByNumber(number);
	}

	@Override
	public Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable) {
		return this.invoiceRepository.findByStatus(status, pageable);
	}

	private Page<Invoice> findByStatusAndNumberIsNotNull(InvoiceStatus status, Pageable pageable) {
		return this.invoiceRepository.findByStatusAndNumberIsNotNull(status, pageable);
	}

	@Override
	public List<Invoice> findByPaidDate(Date paidDate) {
		return this.invoiceRepository.findByPaidDateBetweenAndStatus(paidDate,
				LocalDate.fromDateFields(paidDate).plusDays(1).toDate(),
				new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null));
	}

	@Override
	public Page<Invoice> findByPaidDateBetweenAndStatus(Date startPaidDate, Date startEndDate, InvoiceStatus status,
			Pageable pageable) {
		return this.invoiceRepository.findByPaidDateBetweenAndStatus(startPaidDate, startEndDate, status, pageable);
	}

	@Override
	public Invoice findFirstPaidInvoice() {
		Page<Invoice> page = this.invoiceRepository.findByStatus(new InvoiceStatus(Types.PAID.toString(), null),
				new PageRequest(0, 1, Direction.ASC, "paidDate"));

		if (page.getContent().size() == 0) {
			return null;
		}

		return page.getContent().get(0);
	}

	@Override
	public Integer findNext(int invoiceId) {
		Page<Integer> result = this.invoiceRepository.findIdByIdGreaterThan(invoiceId,
				new PageRequest(0, 1, Direction.ASC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public Invoice findOne(int id) {
		return this.invoiceRepository.findOne(id);
	}

	@Override
	public Integer findPrevious(int invoiceId) {
		Page<Integer> result = this.invoiceRepository.findIdByIdLessThan(invoiceId,
				new PageRequest(0, 1, Direction.DESC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public double findProductQuantitySold(int productId) {
		Double result = this.invoiceRepository.findProductQuantityPurchased(productId);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public Invoice generateNewInvoice(CashRegisterSession session) {
		return this.save(new Invoice(null, null, null, invoiceStatusRepository.findOne(Types.DRAFT.name()), null,
				new Date(), null, null, null, null, 0d, 0d, 0d, new HashMap<Double, InvoiceTaxAmount>(), 0d, 0d, 0d,
				new ArrayList<Payment>(), 0d, 0d, session, null, new ArrayList<InvoiceDetail>()));
	}

	private String generateInvoiceNumber() {
		Page<Invoice> page = this.findByStatusAndNumberIsNotNull(
				this.invoiceStatusRepository.findOne(Types.PAID.name()),
				new PageRequest(0, 1, Direction.DESC, "paidDate"));

		String number = String.format("%010d", 1);
		if (page.getContent().size() > 0 && page.getContent().get(0).getNumber() != null) {
			number = String.format("%010d", Long.valueOf(page.getContent().get(0).getNumber()) + 1);
		}

		return number;
	}

	@Override
	@Transactional
	public void initializeInvoiceNumbers() {
		Page<Invoice> page = null;
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "paidDate");

		String number = "0000000001";

		do {
			page = this.findByStatus(new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null), pageRequest);

			for (Invoice invoice : page.getContent()) {
				invoice.setNumber(number);

				number = String.format("%010d", Long.valueOf(number) + 1);
				;
			}

			this.save(page.getContent());

			pageRequest = pageRequest.next();
		} while (page != null && page.getContent().size() != 0);
	}

	@Override
	public void recalculateInvoiceTaxes() {
		Page<Invoice> invoicePage;
		Pageable pageRequest = new PageRequest(0, 50);
		do {
			invoicePage = this.findAll(pageRequest);

			for (Invoice invoice : invoicePage.getContent()) {
				// this.calculateInvoiceRebatePercent(invoice);
				this.calculateInvoiceTaxes(invoice);
			}

			pageRequest = pageRequest.next();

			this.invoiceRepository.save(invoicePage.getContent());
		} while (invoicePage.hasNext());
	}

	private void updateInventory(Invoice invoice) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			this.productInventoryService.updateInventory(detail.getProduct());

			this.restockService.restockProduct(detail.getProduct(), detail.getQuantity());
		}
	}

	@Override
	public Invoice save(Invoice invoice) {
		return this.save(Arrays.asList(new Invoice[] { invoice })).iterator().next();
	}

	private Iterable<Invoice> save(Iterable<Invoice> invoices) {
		Set<Long> datesForDailyReport = new HashSet<Long>();
		Set<Invoice> paidInvoices = new HashSet<Invoice>();

		for (Invoice invoice : invoices) {
			if (invoice.getId() != null) {
				String status = invoice.getStatus().getType();

				if (invoice.getStatus().equals(Types.CANCELED.name()) && invoice.getCancelDate() == null) {
					invoice.setCancelDate(new Date());
				}

				if (status.equals(Types.PAID.name())) {

					if (invoice.getNumber() == null) {
						String number = this.generateInvoiceNumber();

						invoice.setNumber(number);

						number = String.format("%010d", Long.valueOf(invoice.getNumber()) + 1);
					}

					if (invoice.getPaidDate() == null) {
						invoice.setPaidDate(new Date());

						paidInvoices.add(invoice);
					}
				}
			}

			invoice.setModificationDate(new Date());

			// Calculate invoice amounts.
			this.calculateInvoiceAmounts(invoice);

			if (invoice.getPaidDate() != null) {
				datesForDailyReport.add(
						LocalDate.fromDateFields(invoice.getPaidDate()).toDateTimeAtStartOfDay().toDate().getTime());
			}
		}

		Iterable<Invoice> newInvoices = this.invoiceRepository.save(invoices);

		for (Long dateForDailyReport : datesForDailyReport) {
			this.dailyReportService.generateDailyReport(new Date(dateForDailyReport));
		}

		for (Invoice invoice : paidInvoices) {
			this.updateInventory(invoice);
		}

		return newInvoices;
	}
}
