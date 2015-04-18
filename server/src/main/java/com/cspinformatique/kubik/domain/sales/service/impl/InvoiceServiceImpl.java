package com.cspinformatique.kubik.domain.sales.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.sales.repository.InvoiceRepository;
import com.cspinformatique.kubik.domain.sales.repository.InvoiceStatusRepository;
import com.cspinformatique.kubik.domain.sales.service.CashRegisterSessionService;
import com.cspinformatique.kubik.domain.sales.service.DailyReportService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.CashRegisterSession;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.model.sales.InvoiceTaxAmount;
import com.cspinformatique.kubik.model.sales.Payment;
import com.cspinformatique.kubik.model.sales.InvoiceStatus.Types;

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
	
	private void calculateInvoiceAmounts(Invoice invoice) {
		HashMap<Double, InvoiceTaxAmount> totalTaxesAmounts = new HashMap<Double, InvoiceTaxAmount>();

		double totalAmount = 0d;
		double totalTaxLessAmount = 0d;
		double totalTaxAmount = 0d;
		double totalRebateAmount = 0d;

		if (invoice.getDetails() != null) {
			for (InvoiceDetail detail : invoice.getDetails()) {
				Product product = productService.findOne(detail.getProduct()
						.getId());
				double quantity = detail.getQuantity();
				
				double rebateAmount = 0d;
				if(invoice.getRebatePercent() != null && invoice.getRebatePercent().doubleValue() != 0d){
					rebateAmount = product.getPriceTaxIn() * (invoice.getRebatePercent() / 100);
				} else if(invoice.getRebateAmount() != null && invoice.getRebateAmount().doubleValue() != 0d){
						rebateAmount = invoice.getRebateAmount();
				}				
				totalRebateAmount += rebateAmount;

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
							+ ((product.getPriceTaxIn() - rebateAmount) * (product.getTvaRate1() / 100) * quantity));

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
							+ ((product.getPriceTaxIn() - rebateAmount) * (product.getTvaRate2() / 100) * quantity));

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
							+ ((product.getPriceTaxIn() - rebateAmount) * (product.getTvaRate3() / 100) * quantity));

					detailTaxesAmounts.put(product.getTvaRate3(),
							invoiceTaxAmount);
				}

				// Calculates invoice details amounts
				detail.setUnitPrice(product.getPriceTaxIn());
				detail.setTotalAmount(detail.getUnitPrice() * quantity);

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

		invoice.setRebateAmount(Precision.round(totalRebateAmount, 2));
		invoice.setTotalAmount(Precision.round(totalAmount - totalRebateAmount, 2));
		invoice.setTotalTaxAmount(Precision.round(totalTaxAmount, 2));
		invoice.setTotalTaxLessAmount(Precision.round(totalTaxLessAmount, 2));

		if (invoice.getTaxesAmounts() != null) {
			invoice.getTaxesAmounts().clear();
			invoice.getTaxesAmounts().putAll(totalTaxesAmounts);
		} else {
			invoice.setTaxesAmounts(totalTaxesAmounts);
		}

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
	public InvoiceDetail findDetailByInvoiceIdAndProductEan13(int invoiceId, String ean13){
		return this.invoiceRepository.findDetailByInvoiceIdAndProductEan13(invoiceId, ean13);
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
	public Invoice findByNumber(String number) {
		return this.invoiceRepository.findByNumber(number);
	}

	@Override
	public Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable) {
		return this.invoiceRepository.findByStatus(status, pageable);
	}
	
	private Page<Invoice> findByStatusAndNumberIsNotNull(InvoiceStatus status, Pageable pageable){
		return this.invoiceRepository.findByStatusAndNumberIsNotNull(status, pageable);
	}

	@Override
	public List<Invoice> findByPaidDate(Date paidDate) {
		return this.invoiceRepository.findByPaidDateBetweenAndStatus(paidDate,
				LocalDate.fromDateFields(paidDate).plusDays(1).toDate(),
				new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null));
	}
	
	@Override
	public Page<Invoice> findByPaidDateBetweenAndStatus(Date startPaidDate,
			Date startEndDate, InvoiceStatus status, Pageable pageable){
		return this.invoiceRepository.findByPaidDateBetweenAndStatus(startPaidDate, startEndDate, status, pageable);
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
	public Integer findNext(int invoiceId) {
		Page<Integer> result = this.invoiceRepository.findIdByIdGreaterThan(
				invoiceId, new PageRequest(0, 1, Direction.ASC, "id"));

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
		Page<Integer> result = this.invoiceRepository.findIdByIdLessThan(
				invoiceId, new PageRequest(0, 1, Direction.DESC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}
	
	@Override
	public double findProductQuantitySold(int productId){
		Double result = this.invoiceRepository.findProductQuantityPurchased(productId);
		
		if(result == null){
			return 0d;
		}
		
		return result;
	}

	@Override
	public Invoice generateNewInvoice(CashRegisterSession session) {
		return this.save(new Invoice(null, null, null, invoiceStatusRepository
				.findOne(Types.DRAFT.name()), null, new Date(), null, null,
				null, null, 0d, 0d, 0d,
				new HashMap<Double, InvoiceTaxAmount>(), 0d, 0d, 0d,
				new ArrayList<Payment>(), 0d, 0d, session,
				new ArrayList<InvoiceDetail>()));
	}

	private String generateInvoiceNumber() {
		Page<Invoice> page = this.findByStatusAndNumberIsNotNull(
				this.invoiceStatusRepository.findOne(Types.PAID.name()),
				new PageRequest(0, 1, Direction.DESC, "paidDate"));

		String number = String.format("%09d", 1);
		if (page.getContent().size() > 0 && page.getContent().get(0).getNumber() != null) {
			number = String.format("%09d", Long.valueOf(page.getContent().get(0).getNumber()) + 1);;
		}

		return number;
	}
	
	@Override
	@Transactional
	public void initializeInvoiceNumbers(){
		Page<Invoice> page = null;
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "paidDate");

		String number = "000000001";
		
		do{
			page = this.findByStatus(new InvoiceStatus(InvoiceStatus.Types.PAID.toString(), null), pageRequest);
			
			for(Invoice invoice : page.getContent()){
				invoice.setNumber(number);

				number = String.format("%09d", Long.valueOf(number) + 1);;
			}
			
			this.save(page.getContent());
			
			pageRequest = pageRequest.next();
		}while(page != null && page.getContent().size() != 0);
	}

	private void updateInventory(Invoice invoice) {
		for (InvoiceDetail detail : invoice.getDetails()) {
			this.productInventoryService.updateInventory(detail.getProduct());
		}
	}

	@Override
	public Invoice save(Invoice invoice) {
		return this.save(Arrays.asList(new Invoice[]{invoice})).iterator().next();
	}
	
	private Iterable<Invoice> save(Iterable<Invoice> invoices){
		Set<Long> datesForDailyReport = new HashSet<Long>();
		Set<Invoice> paidInvoices = new HashSet<Invoice>();
		
		String number = this.generateInvoiceNumber();
		
		for(Invoice invoice : invoices){
			if (invoice.getId() != null) {
				String status = invoice.getStatus().getType();

					if (invoice.getStatus().equals(Types.CANCELED.name())
							&& invoice.getCancelDate() == null) {
						invoice.setCancelDate(new Date());
					}
					
					if(status.equals(Types.PAID.name())){
						if(invoice.getNumber() == null){
							invoice.setNumber(number);
							
							number = String.format("%010d", Long.valueOf(invoice.getNumber()) + 1);;
						}
						
						if (invoice.getPaidDate() == null) {
							invoice.setPaidDate(new Date());

							paidInvoices.add(invoice);
						}
					}
			}

			// Calculate invoice amounts.
			this.calculateInvoiceAmounts(invoice);
			
			if(invoice.getPaidDate() != null){
				datesForDailyReport.add(LocalDate.fromDateFields(invoice.getPaidDate()).toDateTimeAtStartOfDay().toDate().getTime());
			}
		}
		
		Iterable<Invoice> newInvoices = this.invoiceRepository.save(invoices);
		
		for(Long dateForDailyReport : datesForDailyReport){
			this.dailyReportService.generateDailyReport(new Date(dateForDailyReport));
		}
		
		for(Invoice invoice : paidInvoices){
			this.updateInventory(invoice);
		}
		
		return newInvoices;
	}
}
