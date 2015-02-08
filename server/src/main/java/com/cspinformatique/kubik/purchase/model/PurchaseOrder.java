package com.cspinformatique.kubik.purchase.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cspinformatique.kubik.product.model.Supplier;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class PurchaseOrder {
	public enum Status{
		DRAFT, CANCELED, SUBMITED, RECEIVED;
	}
	
	private long id;
	private Supplier supplier;
	private Date date;
	private Date dateReceived;
	private String operationCode;
	private ShippingMode shippingMode;
	private NotationCode notationCode;
	private Date minDeliveryDate;
	private Date maxDeliveryDate;
	private List<PurchaseOrderDetail> details;
	private Status status;
	private boolean sentToDilicom;
	private double discount;
	
	public PurchaseOrder(){
		
	}
	
	public PurchaseOrder(long id, Supplier supplier, Date date,
			Date dateReceived, String operationCode, ShippingMode shippingMode,
			NotationCode notationCode, Date minDeliveryDate,
			Date maxDeliveryDate, List<PurchaseOrderDetail> details,
			Status status, boolean sentToDilicom, double discount) {
		this.id = id;
		this.supplier = supplier;
		this.date = date;
		this.dateReceived = dateReceived;
		this.operationCode = operationCode;
		this.shippingMode = shippingMode;
		this.notationCode = notationCode;
		this.minDeliveryDate = minDeliveryDate;
		this.maxDeliveryDate = maxDeliveryDate;
		this.details = details;
		this.status = status;
		this.sentToDilicom = sentToDilicom;
	}

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	@Enumerated(EnumType.STRING)
	@JsonFormat(shape= JsonFormat.Shape.OBJECT)
	public ShippingMode getShippingMode() {
		return shippingMode;
	}

	public void setShippingMode(ShippingMode shippingMode) {
		this.shippingMode = shippingMode;
	}

	@Enumerated(EnumType.STRING)
	public NotationCode getNotationCode() {
		return notationCode;
	}

	public void setNotationCode(NotationCode notationCode) {
		this.notationCode = notationCode;
	}

	public Date getMinDeliveryDate() {
		return minDeliveryDate;
	}

	public void setMinDeliveryDate(Date minDeliveryDate) {
		this.minDeliveryDate = minDeliveryDate;
	}

	public Date getMaxDeliveryDate() {
		return maxDeliveryDate;
	}

	public void setMaxDeliveryDate(Date maxDeliveryDate) {
		this.maxDeliveryDate = maxDeliveryDate;
	}

	@JsonManagedReference
	@OneToMany(cascade=CascadeType.ALL)
	public List<PurchaseOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<PurchaseOrderDetail> details) {
		this.details = details;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isSentToDilicom() {
		return sentToDilicom;
	}

	public void setSentToDilicom(boolean sentToDilicom) {
		this.sentToDilicom = sentToDilicom;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
}
