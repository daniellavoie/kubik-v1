package com.cspinformatique.kubik.server.model.sales;

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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.cspinformatique.kubik.server.model.misc.Address;
import com.cspinformatique.kubik.server.model.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@Table(indexes = @Index(columnList = "paidDate") )
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
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
	private Date confirmedDate;
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
	private Date modificationDate;
	private List<InvoiceDetail> details;
	private Long customerOrderId;
	private Double shippingCost;
	private Address billingAddress;

	public Invoice() {

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
	@NotAudited
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
	@NotAudited
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

	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
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
	@NotAudited
	public CashRegisterSession getCashRegisterSession() {
		return cashRegisterSession;
	}

	public void setCashRegisterSession(CashRegisterSession cashRegisterSession) {
		this.cashRegisterSession = cashRegisterSession;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Long getCustomerOrderId() {
		return customerOrderId;
	}

	public void setCustomerOrderId(Long customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public Double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}

	@ManyToOne
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}
}
