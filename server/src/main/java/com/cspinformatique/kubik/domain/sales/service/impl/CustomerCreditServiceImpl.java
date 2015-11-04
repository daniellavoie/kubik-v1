package com.cspinformatique.kubik.domain.sales.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.sales.repository.CustomerCreditRepository;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.domain.sales.service.DailyReportService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.domain.sales.service.PaymentMethodService;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.Customer;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceTaxAmount;

@Service
public class CustomerCreditServiceImpl implements CustomerCreditService {
	@Autowired
	private CustomerCreditRepository customerCreditRepository;

	@Autowired
	private DailyReportService dailyReportService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private ProductInventoryService productInventoryService;

	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private ProductService productService;

	private void updateInventory(CustomerCredit customerCredit) {
		for (CustomerCreditDetail detail : customerCredit.getDetails()) {
			this.productInventoryService.updateInventory(detail.getProduct());
		}
	}

	private void calculateAmounts(CustomerCredit customerCredit) {

		double totalAmount = 0d;
		double totalRebateAmount = 0d;

		Invoice invoice = this.invoiceService.findOne(customerCredit.getInvoice().getId());

		if (invoice == null) {
			throw new RuntimeException("Invoice " + customerCredit.getInvoice().getId() + " could not be found.");
		}

		if (customerCredit.getDetails() != null) {
			for (CustomerCreditDetail detail : customerCredit.getDetails()) {
				double quantity = detail.getQuantity();

				InvoiceDetail invoiceDetail = this.findInvoiceDetail(invoice, detail);

				if (invoiceDetail == null) {
					throw new RuntimeException("Invoice detail could not be found for product "
							+ detail.getProduct().getId() + " in invoice " + invoice.getId() + ".");
				}

				// Calculate rebate amount for detail.
				double rebateAmount = 0d;
				if (invoice.getRebatePercent() != null && invoice.getRebatePercent().doubleValue() != 0d) {
					rebateAmount = invoiceDetail.getUnitPrice() * (invoice.getRebatePercent() / 100);
				}

				// Increment total rebate amount.
				totalRebateAmount += rebateAmount;

				// Calculates customer credit details amounts
				detail.setUnitPrice(invoiceDetail.getUnitPrice());
				detail.setTotalAmount(invoiceDetail.getUnitPrice() * quantity);

				// Increment customer credit totals amount.
				totalAmount += detail.getTotalAmount();
			}
		}

		totalRebateAmount = Precision.round(totalRebateAmount, 2);
		customerCredit.setRebateAmount(totalRebateAmount);
		customerCredit.setTotalAmount(Precision.round(totalAmount - totalRebateAmount, 2));

		this.calculateTaxesAmounts(customerCredit);
	}

	private void calculateTaxesAmounts(CustomerCredit customerCredit) {
		HashMap<Double, InvoiceTaxAmount> totalTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

		double totalTaxableAmount = 0d;
		double totalTaxAmount = 0d;

		Invoice invoice = this.invoiceService.findOne(customerCredit.getInvoice().getId());

		if (invoice == null) {
			throw new RuntimeException("Invoice " + customerCredit.getInvoice().getId() + " could not be found.");
		}

		for (InvoiceTaxAmount invoiceTaxAmount : customerCredit.getTaxesAmounts().values()) {
			invoiceTaxAmount.setTaxAmount(0d);
			invoiceTaxAmount.setTaxableAmount(0d);
		}

		if (customerCredit.getDetails() != null) {
			for (CustomerCreditDetail detail : customerCredit.getDetails()) {
				Product product = productService.findOne(detail.getProduct().getId());

				double quantity = detail.getQuantity();

				// Calculate rebate amount for detail.
				BigDecimal rebateAmount = new BigDecimal(0d);
				if (invoice.getRebatePercent() != null && invoice.getRebatePercent().doubleValue() != 0d) {
					rebateAmount = new BigDecimal(detail.getUnitPrice())
							.multiply(new BigDecimal(invoice.getRebatePercent()).divide(new BigDecimal(100)));
				}

				BigDecimal detailTotalAmount = new BigDecimal(detail.getUnitPrice()).multiply(new BigDecimal(quantity))
						.subtract(rebateAmount);

				// Increment customer credit taxes amounts.
				InvoiceTaxAmount invoiceTaxAmount = totalTaxesAmounts.get(product.getTvaRate1());
				if (invoiceTaxAmount == null) {
					invoiceTaxAmount = new InvoiceTaxAmount(0, product.getTvaRate1(), 0d, 0d, 0d);
				} else if (invoiceTaxAmount.getTaxedAmount() == null) {
					invoiceTaxAmount.setTaxedAmount(0d);
				}

				invoiceTaxAmount.setTaxedAmount(invoiceTaxAmount.getTaxedAmount() + detailTotalAmount.doubleValue());

				totalTaxesAmounts.put(product.getTvaRate1(), invoiceTaxAmount);
			}
		}

		for (InvoiceTaxAmount invoiceTaxAmount : totalTaxesAmounts.values()) {
			invoiceTaxAmount.setTaxAmount(Precision.round((invoiceTaxAmount.getTaxedAmount()
					/ (1 + (invoiceTaxAmount.getTaxRate() / 100)) * (invoiceTaxAmount.getTaxRate() / 100)), 2));
			invoiceTaxAmount.setTaxableAmount(
					Precision.round(invoiceTaxAmount.getTaxedAmount() - invoiceTaxAmount.getTaxAmount(), 2));

			totalTaxAmount += invoiceTaxAmount.getTaxAmount();
			totalTaxableAmount += invoiceTaxAmount.getTaxableAmount();
		}

		customerCredit.setTotalTaxAmount(Precision.round(totalTaxAmount, 2));
		customerCredit.setTotalTaxLessAmount(Precision.round(totalTaxableAmount, 2));

		if (customerCredit.getTaxesAmounts() != null) {
			customerCredit.getTaxesAmounts().clear();
			customerCredit.getTaxesAmounts().putAll(totalTaxesAmounts);
		} else {
			customerCredit.setTaxesAmounts(totalTaxesAmounts);
		}
	}

	@Override
	public Iterable<CustomerCredit> findAll() {
		return this.customerCreditRepository.findAllOrderByCompleteDateDesc();
	}

	@Override
	public Page<CustomerCredit> findAll(Pageable pageable) {
		return this.customerCreditRepository.findAll(pageable);
	}

	@Override
	public List<CustomerCredit> findByCompleteDate(Date date) {
		return this.customerCreditRepository.findByCompleteDateBetweenAndStatus(
				LocalDate.fromDateFields(date).toDateTimeAtStartOfDay().toDate(),
				LocalDate.fromDateFields(date).plusDays(1).toDate(), Status.COMPLETED);
	}

	@Override
	public Page<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate, Date endDate, Status status,
			Pageable pageable) {
		return this.customerCreditRepository.findByCompleteDateBetweenAndStatus(startDate, endDate, status, pageable);
	}

	@Override
	public List<CustomerCredit> findByInvoice(Invoice invoice) {
		return this.customerCreditRepository.findByInvoice(invoice);
	}

	@Override
	public Page<CustomerCredit> findByStatusAndNumberIsNotNull(Status status, Pageable pageable) {
		return this.customerCreditRepository.findByStatusAndNumberIsNotNull(status, pageable);
	}

	@Override
	public Double findCustomerCreditAvailable(Customer customer) {
		Double customerCreditReceived = this.customerCreditRepository.findCustomerCredit(customer);

		Double customerCreditUsed = this.customerCreditRepository.findCustomerCreditUsed(customer);

		return Precision.round((customerCreditReceived != null ? customerCreditReceived : 0d)
				- (customerCreditUsed != null ? customerCreditUsed : 0d), 2);
	}

	@Override
	public CustomerCredit findOne(int id) {
		return this.customerCreditRepository.findOne(id);
	}

	@Override
	public double findProductQuantityReturnedByCustomer(int productId) {
		Double result = this.customerCreditRepository.findProductQuantityReturnedByCustomer(productId);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	private InvoiceDetail findInvoiceDetail(Invoice invoice, CustomerCreditDetail customerCreditDetail) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			if (detail.getProduct().getId().equals(customerCreditDetail.getProduct().getId())) {
				return detail;
			}
		}

		return null;
	}

	@Override
	public Integer findNext(int customerCreditId) {
		Page<Integer> result = this.customerCreditRepository.findIdByIdGreaterThan(customerCreditId,
				new PageRequest(0, 1, Direction.ASC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public Integer findPrevious(int customerCreditId) {
		Page<Integer> result = this.customerCreditRepository.findIdByIdLessThan(customerCreditId,
				new PageRequest(0, 1, Direction.DESC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	private String generateCustomerCreditNumber() {
		Page<CustomerCredit> page = this.findByStatusAndNumberIsNotNull(CustomerCredit.Status.COMPLETED,
				new PageRequest(0, 1, Direction.DESC, "completeDate"));

		if (page.getContent().size() > 0 && page.getContent().get(0).getNumber() != null) {
			return String.format("%010d", Long.valueOf(page.getContent().get(0).getNumber()) + 1);
		} else {
			return String.format("%010d", 1);
		}
	}

	@Override
	public void recalculateCustomerCreditsTaxes() {
		Page<CustomerCredit> customerCreditPage;
		Pageable pageRequest = new PageRequest(0, 50);
		do {
			customerCreditPage = customerCreditRepository.findAll(pageRequest);

			for (CustomerCredit invoice : customerCreditPage.getContent()) {
				// this.calculateInvoiceRebatePercent(invoice);
				this.calculateTaxesAmounts(invoice);
			}

			pageRequest = pageRequest.next();

			customerCreditRepository.save(customerCreditPage.getContent());
		} while (customerCreditPage.hasNext());
	}

	@Override
	@Transactional
	public CustomerCredit save(CustomerCredit customerCredit) {
		if (customerCredit.getTaxesAmounts() == null) {
			customerCredit.setTaxesAmounts(new HashMap<>());
		}

		if (customerCredit.getPaymentMethod() == null) {
			customerCredit.setPaymentMethod(paymentMethodService.findOne("CREDIT"));
		}

		if (customerCredit.getDate() == null) {
			customerCredit.setDate(new Date());
		}

		this.calculateAmounts(customerCredit);

		boolean customerCreditCompleted = false;
		if (customerCredit.getId() != null) {
			if (customerCredit.getStatus().equals(Status.CANCELED) && customerCredit.getCancelDate() == null) {
				customerCredit.setCancelDate(new Date());
			}

			if (customerCredit.getStatus().equals(Status.COMPLETED) && customerCredit.getCompleteDate() == null) {
				customerCredit.setCompleteDate(new Date());
				customerCreditCompleted = true;

				if (customerCredit.getNumber() == null) {
					customerCredit.setNumber(this.generateCustomerCreditNumber());
				}
			}
		}

		// Validate that other credit has the same customer.
		List<CustomerCredit> existingCredits = this.findByInvoice(customerCredit.getInvoice());

		if (!existingCredits.isEmpty()) {
			CustomerCredit exitingCredit = existingCredits.get(0);
			if (!customerCredit.getCustomer().getId().equals(customerCredit.getCustomer().getId())) {
				throw new RuntimeException("Could not link invoice " + customerCredit.getInvoice().getNumber()
						+ " to customer " + customerCredit.getCustomer().getId()
						+ " as it is already linked to customer " + exitingCredit.getCustomer().getId());
			}
		}

		customerCredit = this.customerCreditRepository.save(customerCredit);

		this.customerCreditRepository.flush();

		if (customerCreditCompleted) {
			this.updateInventory(customerCredit);

			this.dailyReportService.generateDailyReport(new Date());
		}

		return customerCredit;
	}
}
