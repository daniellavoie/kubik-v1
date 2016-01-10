package com.cspinformatique.kubik.kos.model.order;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.account.Address;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "uuid") )
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
	private String uuid;
	private List<CustomerOrderDetail> customerOrderDetails;
	private Account account;
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
	private ShippingCostLevel applicableShippingCostLevel;
	private ShippingMethod shippingMethod;
	private Address shippingAddress;
	private Address billingAddress;
	private TransactionGateway transactionGateway;
	private String transactionId;
	private Long invoiceId;

	public CustomerOrder() {

	}

	public CustomerOrder(long id, String uuid, List<CustomerOrderDetail> customerOrderDetails, Account account,
			Status status, Date openDate, Date abandonedDate, Date toConfirmDate, Date confirmedDate,
			Date processedDate, Date errorDate, Double subTotal, Double totalAmount, int totalQuantity,
			Integer totalWeight, Double shippingCost, ShippingCostLevel applicableShippingCostLevels,
			ShippingMethod shippingMethod, Address shippingAddress, Address billingAddress,
			TransactionGateway transactionGateway, String transactionId, Long invoiceId) {
		this.id = id;
		this.uuid = uuid;
		this.customerOrderDetails = customerOrderDetails;
		this.account = account;
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
		this.applicableShippingCostLevel = applicableShippingCostLevels;
		this.shippingMethod = shippingMethod;
		this.shippingAddress = shippingAddress;
		this.billingAddress = billingAddress;
		this.transactionGateway = transactionGateway;
		this.transactionId = transactionId;
		this.invoiceId = invoiceId;
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerOrderDetail> getCustomerOrderDetails() {
		return customerOrderDetails;
	}

	public void setCustomerOrderDetails(List<CustomerOrderDetail> customerOrderDetails) {
		this.customerOrderDetails = customerOrderDetails;
	}

	@ManyToOne
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	@ManyToOne
	public ShippingCostLevel getApplicableShippingCostLevel() {
		return applicableShippingCostLevel;
	}

	public void setApplicableShippingCostLevel(ShippingCostLevel applicableShippingCostLevels) {
		this.applicableShippingCostLevel = applicableShippingCostLevels;
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

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}
}
