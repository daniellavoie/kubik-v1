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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class CustomerCredit {
	public enum Status {
		CANCELED, COMPLETED, OPEN
	};

	private Date cancelDate;	
	private Date completeDate;
	private Customer customer;
	private Date date;
	private List<CustomerCreditDetail> details;
	private Integer id;
	private Invoice invoice;
	private String number;
	private PaymentMethod paymentMethod;
	private Double rebateAmount;
	private Status status;
	private Map<Double, InvoiceTaxAmount> taxesAmounts;
	private Double totalAmount;
	private Double totalAmountRebateOut;
	private Double totalTaxAmount;
	private Double totalTaxLessAmount;

	public CustomerCredit() {

	}

	public CustomerCredit(Integer id, String number, Invoice invoice, Customer customer,
			List<CustomerCreditDetail> details, Status status, Date date,
			Date cancelDate, Date completeDate, PaymentMethod paymentMethod, Double rebateAmount,
			Map<Double, InvoiceTaxAmount> taxesAmounts,
			Double totalTaxLessAmount, Double totalTaxAmount,
			Double totalAmount, Double totalAmountRebateOut) {
		this.id = id;
		this.number = number;
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

	public Date getCancelDate() {
		return cancelDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	@ManyToOne
	public Customer getCustomer() {
		return customer;
	}

	public Date getDate() {
		return date;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerCreditDetail> getDetails() {
		return details;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	@ManyToOne
	public Invoice getInvoice() {
		return invoice;
	}

	public String getNumber() {
		return number;
	}

	@ManyToOne
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public Double getRebateAmount() {
		return rebateAmount;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public Map<Double, InvoiceTaxAmount> getTaxesAmounts() {
		return taxesAmounts;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public Double getTotalAmountRebateOut() {
		return totalAmountRebateOut;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public Double getTotalTaxLessAmount() {
		return totalTaxLessAmount;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDetails(List<CustomerCreditDetail> details) {
		this.details = details;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTaxesAmounts(Map<Double, InvoiceTaxAmount> taxesAmounts) {
		this.taxesAmounts = taxesAmounts;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTotalAmountRebateOut(Double totalAmountRebateOut) {
		this.totalAmountRebateOut = totalAmountRebateOut;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public void setTotalTaxLessAmount(Double totalTaxLessAmount) {
		this.totalTaxLessAmount = totalTaxLessAmount;
	}
}
