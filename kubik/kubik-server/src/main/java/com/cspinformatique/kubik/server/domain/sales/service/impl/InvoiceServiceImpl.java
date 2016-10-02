package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.purchase.service.RestockService;
import com.cspinformatique.kubik.server.domain.reporting.service.ProductSaleService;
import com.cspinformatique.kubik.server.domain.sales.repository.InvoiceRepository;
import com.cspinformatique.kubik.server.domain.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.server.domain.sales.service.DailyReportService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceAmountsService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.domain.sales.service.ShippingCostLevelService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.CashRegisterSession;
import com.cspinformatique.kubik.server.model.sales.Customer;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.Invoice.ShippingMethod;
import com.cspinformatique.kubik.server.model.sales.Invoice.Source;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus.Types;
import com.cspinformatique.kubik.server.model.sales.InvoiceTaxAmount;
import com.cspinformatique.kubik.server.model.sales.Payment;
import com.querydsl.core.types.Predicate;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	@Resource
	CustomerService customerService;

	@Resource
	DailyReportService dailyReportService;

	@Resource
	InvoiceAmountsService invoiceAmountsService;

	@Resource
	InvoiceRepository invoiceRepository;

	@Resource
	InvoiceStatusRepository invoiceStatusRepository;

	@Resource
	ProductInventoryService productInventoryService;

	@Resource
	ProductSaleService productSaleService;

	@Resource
	ProductService productService;

	@Resource
	RestockService restockService;

	@Resource
	ShippingCostLevelService shippingCostLevelService;

	@Resource
	private StocktakingService stocktakingService;

	private void calculateInvoiceAmounts(Invoice invoice) {
		int totalWeight = 0;
		double totalAmount = 0d;
		double totalRebateAmount = 0d;

		if (invoice.getDetails() != null) {
			for (InvoiceDetail detail : invoice.getDetails()) {
				Product product = productService.findOne(detail.getProduct().getId());
				detail.setTaxRate(product.getTvaRate1());

				// Calculates customer credit details amounts
				detail.setProduct(product);
				detail.setUnitPrice(product.getPriceTaxIn());
				
				invoiceAmountsService.calculateDetailAmounts(detail);

				// Increment total rebate amount.
				totalRebateAmount += detail.getRebate();

				// Increment customer credit totals amount.
				totalAmount += detail.getTotalAmount();

				totalWeight += detail.getQuantity() + (product.getWeight() != null ? product.getWeight() : 0d);
			}
		}

		if (!Types.PAID.name().equals(invoice.getStatus().getType())
				&& ShippingMethod.COLISSIMO.equals(invoice.getShippingMethod()))
			invoice.setShippingCost(shippingCostLevelService.calculateShippingCostLevel(totalWeight).getCost());

		if (!Types.PAID.name().equals(invoice.getStatus().getType())
				&& ShippingMethod.TAKEOUT.equals(invoice.getShippingMethod()))
			invoice.setShippingCost(0d);

		if (invoice.getShippingCost() != null) {
			totalAmount += invoice.getShippingCost();
		}

		totalRebateAmount = Precision.round(totalRebateAmount, 2);
		invoice.setRebateAmount(totalRebateAmount);
		invoice.setTotalAmount(Precision.round(totalAmount, 2));

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
		invoice.setTotalWeight(totalWeight);
	}

	@Override
	public void calculateInvoiceTaxes(Invoice invoice) {
		HashMap<Double, InvoiceTaxAmount> totalTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

		double totalTaxableAmount = 0d;
		double totalTaxAmount = 0d;

		if (invoice.getTaxesAmounts() != null) {
			for (InvoiceTaxAmount invoiceTaxAmount : invoice.getTaxesAmounts().values()) {
				invoiceTaxAmount.setTaxAmount(0d);
				invoiceTaxAmount.setTaxableAmount(0d);
				invoiceTaxAmount.setTaxedAmount(0d);
			}
		}

		if (invoice.getDetails() != null) {
			for (InvoiceDetail detail : invoice.getDetails()) {
				Product product = productService.findOne(detail.getProduct().getId());

				// Increment taxed amounts.
				InvoiceTaxAmount invoiceTaxAmount = totalTaxesAmounts.get(product.getTvaRate1());
				if (invoiceTaxAmount == null) {
					invoiceTaxAmount = new InvoiceTaxAmount(0, product.getTvaRate1(), 0d, 0d, 0d);
				} else if (invoiceTaxAmount.getTaxedAmount() == null) {
					invoiceTaxAmount.setTaxedAmount(0d);
				}
				invoiceTaxAmount.setTaxedAmount(detail.getTotalAmount() + invoiceTaxAmount.getTaxedAmount());

				totalTaxesAmounts.put(product.getTvaRate1(), invoiceTaxAmount);
			}
		}

		if (totalTaxesAmounts != null) {
			for (InvoiceTaxAmount invoiceTaxAmount : totalTaxesAmounts.values()) {
				invoiceTaxAmount.setTaxAmount(Precision.round((invoiceTaxAmount.getTaxedAmount()
						/ (1 + (invoiceTaxAmount.getTaxRate() / 100)) * (invoiceTaxAmount.getTaxRate() / 100)), 2));
				invoiceTaxAmount.setTaxableAmount(
						Precision.round(invoiceTaxAmount.getTaxedAmount() - invoiceTaxAmount.getTaxAmount(), 2));

				totalTaxAmount += invoiceTaxAmount.getTaxAmount();
				totalTaxableAmount += invoiceTaxAmount.getTaxableAmount();
			}
		}

		invoice.setTotalTaxAmount(Precision.round(totalTaxAmount, 2));

		// Adds shipping fees to tax less amount.
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
	public Page<Invoice> findAll(Predicate predicate, Pageable pageable) {
		return invoiceRepository.findAll(predicate, pageable);
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
	public List<Invoice> findByStatusAndPaidDateAfter(InvoiceStatus status, Date paidDate) {
		return invoiceRepository.findByStatusAndPaidDateAfter(status, paidDate);
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
	public double findProductQuantityOnHold(int productId) {
		Double result = invoiceRepository.findProductQuantityOnHold(productId);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public double findProductQuantityOnHoldUntil(int productId, Date until) {
		Double result = invoiceRepository.findProductQuantityOnHoldUntil(productId, until);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public double findProductQuantitySold(int productId) {
		Double result = this.invoiceRepository.findProductQuantitySold(productId);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public double findProductQuantitySoldUntil(int productId, Date until) {
		Double result = this.invoiceRepository.findProductQuantitySoldUntil(productId, until);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public Invoice generateNewInvoice(Long customerOrderId, Double shippingCost, ShippingMethod shippingMethod) {
		return generateNewInvoice(invoiceStatusRepository.findOne(Types.DRAFT.name()), null, null, customerOrderId,
				shippingCost, Source.WEB_ORDER, shippingMethod);
	}

	@Override
	public Invoice generateNewInvoice(CashRegisterSession session) {
		return generateNewInvoice(invoiceStatusRepository.findOne(Types.DRAFT.name()), null, session, null, null,
				Source.CASH_REGISTER, ShippingMethod.TAKEOUT);
	}

	private Invoice generateNewInvoice(InvoiceStatus status, Customer customer, CashRegisterSession session,
			Long customerOrderId, Double shippingCost, Source source, ShippingMethod shippingMethod) {
		Invoice invoice = new Invoice();
		invoice.setStatus(status);
		invoice.setCustomer(customer);
		invoice.setDate(Date.from(LocalDateTime.now().withNano(0).atZone(ZoneId.systemDefault()).toInstant()));
		invoice.setCashRegisterSession(session);
		invoice.setShippingCost(shippingCost);
		invoice.setDetails(new ArrayList<>());
		invoice.setPayments(new ArrayList<>());
		invoice.setSource(source);
		invoice.setShippingMethod(shippingMethod);

		return this.save(invoice);
	}

	@Override
	public Invoice generateNewOrder(int customerId) {
		return generateNewInvoice(invoiceStatusRepository.findOne(Types.ORDER.name()),
				customerService.findOne(customerId), null, null, null, Source.CUSTOMER_ORDER, ShippingMethod.TAKEOUT);

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

	public void recalculateDetailsAmounts() {
		Page<Invoice> invoicePage;
		Pageable pageRequest = new PageRequest(0, 50);
		do {
			invoicePage = this.findAll(pageRequest);

			for (Invoice invoice : invoicePage.getContent()) {
				for (InvoiceDetail invoiceDetail : invoice.getDetails())
					invoiceAmountsService.calculateDetailAmounts(invoiceDetail);
			}

			pageRequest = pageRequest.next();

			this.invoiceRepository.save(invoicePage.getContent());
		} while (invoicePage.hasNext());
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
			productInventoryService.updateInventory(detail.getProduct());

			stocktakingService.applyInventoryAdjustments(detail.getProduct(), detail.getQuantity() * -1);
			restockService.restockProduct(detail.getProduct(), detail.getQuantity());
			productSaleService.update(invoice);
		}
	}

	@Override
	public Invoice save(Invoice invoice) {
		return this.save(Arrays.asList(new Invoice[] { invoice })).iterator().next();
	}

	private Iterable<Invoice> save(Iterable<Invoice> invoices) {
		Set<Long> datesForDailyReport = new HashSet<Long>();
		Set<Invoice> invoicesWithAlteredInventory = new HashSet<Invoice>();

		for (Invoice invoice : invoices) {
			String status = invoice.getStatus().getType();

			if (invoice.getId() != null) {
				if (Types.CANCELED.name().equals(status) && invoice.getCancelDate() == null) {
					invoice.setCancelDate(new Date());

					if (invoice.getConfirmedDate() != null) {
						invoicesWithAlteredInventory.add(invoice);
					}
				}

				if (Types.ORDER_CONFIRMED.name().equals(status) && invoice.getConfirmedDate() == null) {
					invoice.setConfirmedDate(new Date());

					invoicesWithAlteredInventory.add(invoice);
				}

				if (Types.PAID.name().equals(status)) {
					if (invoice.getNumber() == null) {
						String number = this.generateInvoiceNumber();

						invoice.setNumber(number);

						number = String.format("%010d", Long.valueOf(invoice.getNumber()) + 1);
					}

					if (invoice.getPaidDate() == null) {
						invoice.setPaidDate(new Date());

						invoicesWithAlteredInventory.add(invoice);
					}

				}
			}

			invoice.setModificationDate(
					Date.from(LocalDateTime.now().withNano(0).atZone(ZoneId.systemDefault()).toInstant()));

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

		for (Invoice invoice : invoicesWithAlteredInventory) {
			this.updateInventory(invoice);
		}

		return newInvoices;
	}
}
