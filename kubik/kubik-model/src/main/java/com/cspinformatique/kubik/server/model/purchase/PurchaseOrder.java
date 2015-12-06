package com.cspinformatique.kubik.server.model.purchase;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.cspinformatique.kubik.server.model.dilicom.DilicomOrder;
import com.cspinformatique.kubik.server.model.product.Supplier;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class PurchaseOrder {
	public enum Status {
		DRAFT, CANCELED, SUBMITED, RECEIVED;
	}

	private long id;
	private Supplier supplier;
	private Date date;
	private Date submitedDate;
	private String operationCode;
	private ShippingMode shippingMode;
	private NotationCode notationCode;
	private Date minDeliveryDate;
	private Date maxDeliveryDate;
	private List<PurchaseOrderDetail> details;
	private Status status;
	private DilicomOrder dilicomOrder;

	private float discount;
	private double totalAmountTaxOut;

	private PurchaseSession purchaseSession;
	private Reception reception;

	public PurchaseOrder() {

	}

	public PurchaseOrder(long id, Supplier supplier, Date date,
			Date submitedDate, String operationCode, ShippingMode shippingMode,
			NotationCode notationCode, Date minDeliveryDate,
			Date maxDeliveryDate, List<PurchaseOrderDetail> details,
			Status status, DilicomOrder dilicomOrder, float discount,
			double totalAmountTaxOut, PurchaseSession purchaseSession, Reception reception) {
		this.id = id;
		this.supplier = supplier;
		this.date = date;
		this.submitedDate = submitedDate;
		this.operationCode = operationCode;
		this.shippingMode = shippingMode;
		this.notationCode = notationCode;
		this.minDeliveryDate = minDeliveryDate;
		this.maxDeliveryDate = maxDeliveryDate;
		this.details = details;
		this.status = status;
		this.dilicomOrder = dilicomOrder;
		this.discount = discount;
		this.totalAmountTaxOut = totalAmountTaxOut;
		this.purchaseSession = purchaseSession;
		this.reception = reception;
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

	public Date getSubmitedDate() {
		return submitedDate;
	}

	public void setSubmitedDate(Date submitedDate) {
		this.submitedDate = submitedDate;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	@Enumerated(EnumType.STRING)
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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

	@OrderBy("id ASC")
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
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

	@OneToOne(cascade = CascadeType.ALL)
	public DilicomOrder getDilicomOrder() {
		return dilicomOrder;
	}

	public void setDilicomOrder(DilicomOrder dilicomOrder) {
		this.dilicomOrder = dilicomOrder;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public double getTotalAmountTaxOut() {
		return totalAmountTaxOut;
	}

	public void setTotalAmountTaxOut(double totalAmountTaxOut) {
		this.totalAmountTaxOut = totalAmountTaxOut;
	}

	@ManyToOne
	@JsonBackReference
	public PurchaseSession getPurchaseSession() {
		return purchaseSession;
	}

	public void setPurchaseSession(PurchaseSession purchaseSession) {
		this.purchaseSession = purchaseSession;
	}

	@OneToOne
	public Reception getReception() {
		return reception;
	}

	@OneToOne
	public void setReception(Reception reception) {
		this.reception = reception;
	}
}
