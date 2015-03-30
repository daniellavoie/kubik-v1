package com.cspinformatique.kubik.domain.sales.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cspinformatique.kubik.domain.sales.service.CustomerService;
import com.cspinformatique.kubik.domain.sales.service.DailyReportService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.domain.sales.service.PaymentMethodService;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.Customer;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceTaxAmount;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;

@Service
public class CustomerCreditServiceImpl implements CustomerCreditService {
	@Autowired
	private CustomerCreditRepository customerCreditRepository;

	@Autowired
	private CustomerService customerService;

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
						creditTaxAmount = new InvoiceTaxAmount(null,
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
	public List<CustomerCredit> findByCompleteDate(Date date) {
		return this.customerCreditRepository
				.findByCompleteDateBetweenAndStatus(
						LocalDate.fromDateFields(date).toDateTimeAtStartOfDay()
								.toDate(), LocalDate.fromDateFields(date)
								.plusDays(1).toDate(), Status.COMPLETED);
	}

	@Override
	public Page<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate, Date endDate, Status status, Pageable pageable){
		return this.customerCreditRepository.findByCompleteDateBetweenAndStatus(startDate, endDate, status, pageable);
	}
	
	@Override
	public List<CustomerCredit> findByInvoice(Invoice invoice) {
		return this.customerCreditRepository.findByInvoice(invoice);
	}

	@Override
	public Double findCustomerCreditAvailable(Customer customer) {
		Double customerCreditReceived = this.customerCreditRepository
				.findCustomerCredit(customer);

		Double customerCreditUsed = this.customerCreditRepository
				.findCustomerCreditUsed(customer);

		return (customerCreditReceived != null ? customerCreditReceived : 0d)
				- (customerCreditUsed != null ? customerCreditUsed : 0d);
	}

	@Override
	public CustomerCredit findOne(int id) {
		return this.customerCreditRepository.findOne(id);
	}
	
	@Override
	public double findProductQuantityReturned(int productId){
		Double result = this.customerCreditRepository.findProductQuantityReturned(productId);
		
		if(result == null){
			return 0d;
		}
		
		return result;
	}

	private InvoiceDetail findInvoiceDetail(Invoice invoice,
			CustomerCreditDetail customerCreditDetail) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			if (detail.getProduct().getId()
					.equals(customerCreditDetail.getProduct().getId())) {
				return detail;
			}
		}

		return null;
	}

	@Override
	public Integer findNext(int customerCreditId) {
		Page<Integer> result = this.customerCreditRepository
				.findIdByIdGreaterThan(customerCreditId, new PageRequest(0, 1,
						Direction.ASC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public Integer findPrevious(int customerCreditId) {
		Page<Integer> result = this.customerCreditRepository
				.findIdByIdLessThan(customerCreditId, new PageRequest(0, 1,
						Direction.DESC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	@Transactional
	public CustomerCredit save(CustomerCredit customerCredit) {
		if (customerCredit.getPaymentMethod() == null) {
			customerCredit.setPaymentMethod(paymentMethodService
					.findOne("CREDIT"));
		}

		if (customerCredit.getDate() == null) {
			customerCredit.setDate(new Date());
		}

		this.calculateAmounts(customerCredit);

		boolean customerCreditCompleted = false;
		if (customerCredit.getId() != null) {
			if(customerCredit.getStatus().equals(Status.CANCELED) && customerCredit.getCancelDate() == null){
				customerCredit.setCancelDate(new Date());
			}
			
			if(customerCredit.getStatus().equals(Status.COMPLETED) && customerCredit.getCompleteDate() == null){
				customerCredit.setCompleteDate(new Date());
				customerCreditCompleted = true;
			}
		}
		
		// Validate that other credit has the same customer.
		List<CustomerCredit> existingCredits = this
				.findByInvoice(customerCredit.getInvoice());

		if (!existingCredits.isEmpty()) {
			CustomerCredit exitingCredit = existingCredits.get(0);
			if (!customerCredit.getCustomer().getId()
					.equals(customerCredit.getCustomer().getId())) {
				throw new RuntimeException(
						"Could not link invoice "
								+ customerCredit.getInvoice()
										.getNumber()
								+ " to customer "
								+ customerCredit.getCustomer()
										.getId()
								+ " as it is already linked to customer "
								+ exitingCredit.getCustomer()
										.getId());
			}
		}

		customerCredit = this.customerCreditRepository.save(customerCredit);
		
		if(customerCreditCompleted){
			this.updateInventory(customerCredit);

			this.dailyReportService.generateDailyReport(new Date());
		}
		
		return customerCredit;
	}
}
