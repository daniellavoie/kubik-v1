package com.cspinformatique.kubik.model.sales;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class CustomerCredit {
	public enum Status {
		OPEN, COMPLETED, CANCELED
	};

	private Integer id;
	private Invoice invoice;
	private Customer customer;
	private List<CustomerCreditDetail> details;
	private Status status;
	private Date date;
	private Date cancelDate;
	private Date completeDate;
	
	private PaymentMethod paymentMethod;

	private Double rebateAmount;
	private Map<Double, InvoiceTaxAmount> taxesAmounts;
	private Double totalTaxLessAmount;
	private Double totalTaxAmount;
	private Double totalAmount;
	private Double totalAmountRebateOut;

	public CustomerCredit() {

	}

	public CustomerCredit(Integer id, Invoice invoice, Customer customer,
			List<CustomerCreditDetail> details, Status status, Date date,
			Date cancelDate, Date completeDate, PaymentMethod paymentMethod, Double rebateAmount,
			Map<Double, InvoiceTaxAmount> taxesAmounts,
			Double totalTaxLessAmount, Double totalTaxAmount,
			Double totalAmount, Double totalAmountRebateOut) {
		this.id = id;
		this.invoice = invoice;
		this.customer = customer;
		this.details = details;
		this.status = status;
		this.date = date;
		this.cancelDate = cancelDate;
		this.completeDate = completeDate;
		this.paymentMethod = paymentMethod;
		this.rebateAmount = rebateAmount;
		this.taxesAmounts = taxesAmounts;
		this.totalTaxLessAmount = totalTaxLessAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalAmount = totalAmount;
		this.totalAmountRebateOut = totalAmountRebateOut;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@ManyToOne
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerCreditDetail> getDetails() {
		return details;
	}

	public void setDetails(List<CustomerCreditDetail> details) {
		this.details = details;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	@ManyToOne
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Double getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public Map<Double, InvoiceTaxAmount> getTaxesAmounts() {
		return taxesAmounts;
	}

	public void setTaxesAmounts(Map<Double, InvoiceTaxAmount> taxesAmounts) {
		this.taxesAmounts = taxesAmounts;
	}

	public Double getTotalTaxLessAmount() {
		return totalTaxLessAmount;
	}

	public void setTotalTaxLessAmount(Double totalTaxLessAmount) {
		this.totalTaxLessAmount = totalTaxLessAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalAmountRebateOut() {
		return totalAmountRebateOut;
	}

	public void setTotalAmountRebateOut(Double totalAmountRebateOut) {
		this.totalAmountRebateOut = totalAmountRebateOut;
	}
}
