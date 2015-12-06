package com.cspinformatique.kubik.server.model.sales;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cspinformatique.kubik.server.model.misc.Address;

@Entity
public class CustomerOrder {
	public enum Status {
		OPEN, ABANDONED, TO_CONFIRM, CONFIRMED, PROCESSED, ERROR
	}

	public enum ShippingMethod {
		TAKEOUT, COLISSIMO
	}

	public enum TransactionGateway {
		BRAINTREE
	}

	private long id;
	private long kosId;
	private List<CustomerOrderDetail> customerOrderDetails;
	private Customer customer;
	private Status status;
	private Date openDate;
	private Date abandonedDate;
	private Date toConfirmDate;
	private Date confirmedDate;
	private Date processedDate;
	private Date errorDate;
	private Double subTotal;
	private Double totalAmount;
	private int totalQuantity;
	private Integer totalWeight;
	private Double shippingCost;
	private ShippingMethod shippingMethod;
	private Address shippingAddress;
	private Address billingAddress;
	private TransactionGateway transactionGateway;
	private String transactionId;

	public CustomerOrder() {

	}

	public CustomerOrder(long id, long kosId, String uuid, List<CustomerOrderDetail> customerOrderDetails,
			Customer customer, Status status, Date openDate, Date abandonedDate, Date toConfirmDate, Date confirmedDate,
			Date processedDate, Date errorDate, Double subTotal, Double totalAmount, int totalQuantity,
			Integer totalWeight, Double shippingCost, ShippingMethod shippingMethod, Address shippingAddress,
			Address billingAddress, TransactionGateway transactionGateway, String transactionId) {
		this.id = id;
		this.kosId = kosId;
		this.customerOrderDetails = customerOrderDetails;
		this.customer = customer;
		this.status = status;
		this.openDate = openDate;
		this.abandonedDate = abandonedDate;
		this.toConfirmDate = toConfirmDate;
		this.confirmedDate = confirmedDate;
		this.processedDate = processedDate;
		this.errorDate = errorDate;
		this.subTotal = subTotal;
		this.totalAmount = totalAmount;
		this.totalQuantity = totalQuantity;
		this.totalWeight = totalWeight;
		this.shippingCost = shippingCost;
		this.shippingMethod = shippingMethod;
		this.shippingAddress = shippingAddress;
		this.billingAddress = billingAddress;
		this.transactionGateway = transactionGateway;
		this.transactionId = transactionId;
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKosId() {
		return kosId;
	}

	public void setKosId(long kosId) {
		this.kosId = kosId;
	}

	@OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerOrderDetail> getCustomerOrderDetails() {
		return customerOrderDetails;
	}

	public void setCustomerOrderDetails(List<CustomerOrderDetail> customerOrderDetails) {
		this.customerOrderDetails = customerOrderDetails;
	}

	@ManyToOne
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getAbandonedDate() {
		return abandonedDate;
	}

	public void setAbandonedDate(Date abandonedDate) {
		this.abandonedDate = abandonedDate;
	}

	public Date getToConfirmDate() {
		return toConfirmDate;
	}

	public void setToConfirmDate(Date toConfirmDate) {
		this.toConfirmDate = toConfirmDate;
	}

	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public Date getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Integer getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Integer totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}

	@Enumerated(EnumType.STRING)
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@ManyToOne
	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@ManyToOne
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Enumerated(EnumType.STRING)
	public TransactionGateway getTransactionGateway() {
		return transactionGateway;
	}

	public void setTransactionGateway(TransactionGateway transactionGateway) {
		this.transactionGateway = transactionGateway;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
