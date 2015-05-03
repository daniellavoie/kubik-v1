package com.cspinformatique.kubik.model.sales;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cspinformatique.kubik.model.security.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(indexes=@Index(columnList="paidDate"))
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Invoice {
	private Integer id;
	private String number;
	private User user;
	private InvoiceStatus status;
	private Customer customer;
	private Date date;
	private Date cancelDate;
	private Date invoiceDate;
	private Date paidDate;
	private Date refundDate;
	private Double totalTaxLessAmount;
	private Double rebateAmount;
	private Double rebatePercent;
	private Map<Double, InvoiceTaxAmount> taxesAmounts;
	private Double totalTaxAmount;
	private Double totalAmount;
	private Double totalAmountRebateOut;
	private List<Payment> payments;
	private Double amountPaid;
	private Double amountReturned;
	private CashRegisterSession cashRegisterSession;

	private List<InvoiceDetail> details;

	public Invoice() {

	}

	public Invoice(Integer id, String number, User user, InvoiceStatus status,
			Customer customer, Date date, Date cancelDate, Date invoiceDate,
			Date paidDate, Date refundDate, Double totalTaxLessAmount,
			Double rebateAmount, Double rebatePercent,
			Map<Double, InvoiceTaxAmount> taxesAmounts, Double totalTaxAmount,
			Double totalAmount, Double totalAmountRebateOut,
			List<Payment> payments, Double amountPaid, Double amountReturned,
			CashRegisterSession cashRegisterSession, List<InvoiceDetail> details) {
		this.id = id;
		this.number = number;
		this.user = user;
		this.status = status;
		this.customer = customer;
		this.date = date;
		this.cancelDate = cancelDate;
		this.invoiceDate = invoiceDate;
		this.paidDate = paidDate;
		this.refundDate = refundDate;
		this.totalTaxLessAmount = totalTaxLessAmount;
		this.rebateAmount = rebateAmount;
		this.rebatePercent = rebatePercent;
		this.taxesAmounts = taxesAmounts;
		this.totalTaxAmount = totalTaxAmount;
		this.totalAmount = totalAmount;
		this.totalAmountRebateOut = totalAmountRebateOut;
		this.payments = payments;
		this.amountPaid = amountPaid;
		this.amountReturned = amountReturned;
		this.cashRegisterSession = cashRegisterSession;
		this.details = details;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	public InvoiceStatus getStatus() {
		return status;
	}

	public void setInvoiceStatus(InvoiceStatus status) {
		this.status = status;
	}

	@ManyToOne
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public Double getTotalTaxLessAmount() {
		return totalTaxLessAmount;
	}

	public void setTotalTaxLessAmount(Double subTotal) {
		this.totalTaxLessAmount = subTotal;
	}

	public Double getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public Double getRebatePercent() {
		return rebatePercent;
	}

	public void setRebatePercent(Double rebatePercent) {
		this.rebatePercent = rebatePercent;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public Map<Double, InvoiceTaxAmount> getTaxesAmounts() {
		return taxesAmounts;
	}

	public void setTaxesAmounts(Map<Double, InvoiceTaxAmount> taxesAmounts) {
		this.taxesAmounts = taxesAmounts;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double tvaAmount) {
		this.totalTaxAmount = tvaAmount;
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

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getAmountReturned() {
		return amountReturned;
	}

	public void setAmountReturned(Double amountReturned) {
		this.amountReturned = amountReturned;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	public List<InvoiceDetail> getDetails() {
		return details;
	}

	public void setDetails(List<InvoiceDetail> details) {
		this.details = details;
	}

	@ManyToOne
	public CashRegisterSession getCashRegisterSession() {
		return cashRegisterSession;
	}

	public void setCashRegisterSession(CashRegisterSession cashRegisterSession) {
		this.cashRegisterSession = cashRegisterSession;
	}
}
