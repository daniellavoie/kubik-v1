package com.cspinformatique.kubik.model.purchase;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.cspinformatique.kubik.model.product.Supplier;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Reception {
	public enum Status{
		STANDBY, SHIPPED, IN_PROGRESS, CLOSED, CANCELED;
	}
	
	private Integer id;
	private Supplier supplier;
	private ShippingMode shippingMode;
	private Date dateCreated;
	private Date dateReceived;
	private DeliveryDateType deliveryDateType;
	private Date deliveryDate;
	private PurchaseOrder purchaseOrder;
	private List<ReceptionDetail> details;
	private Status status;
	private List<ShippingPackage> shippingPackages;
	
	private float discount;
	private double totalAmountTaxOut;
	private boolean editable;
	
	public Reception(){
		
	}

	public Reception(Integer id, Supplier supplier, ShippingMode shippingMode,
			Date dateCreated, Date dateReceived,
			DeliveryDateType deliveryDateType, Date deliveryDate,
			PurchaseOrder purchaseOrder, List<ReceptionDetail> details,
			Status status, List<ShippingPackage> shippingPackages,
			float discount, double totalAmountTaxOut, boolean editable) {
		this.id = id;
		this.supplier = supplier;
		this.shippingMode = shippingMode;
		this.dateCreated = dateCreated;
		this.dateReceived = dateReceived;
		this.deliveryDateType = deliveryDateType;
		this.deliveryDate = deliveryDate;
		this.purchaseOrder = purchaseOrder;
		this.details = details;
		this.status = status;
		this.shippingPackages = shippingPackages;
		this.discount = discount;
		this.totalAmountTaxOut = totalAmountTaxOut;
		this.editable = editable;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Enumerated(EnumType.STRING)
	@JsonFormat(shape= JsonFormat.Shape.OBJECT)
	public ShippingMode getShippingMode() {
		return shippingMode;
	}

	public void setShippingMode(ShippingMode shippingMode) {
		this.shippingMode = shippingMode;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public DeliveryDateType getDeliveryDateType() {
		return deliveryDateType;
	}

	public void setDeliveryDateType(DeliveryDateType deliveryDateType) {
		this.deliveryDateType = deliveryDateType;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@OneToOne
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@OrderBy("id ASC")
	@OneToMany(cascade=CascadeType.ALL)
	public List<ReceptionDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ReceptionDetail> details) {
		this.details = details;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	public List<ShippingPackage> getShippingPackages() {
		return shippingPackages;
	}

	public void setShippingPackages(List<ShippingPackage> shippingPackages) {
		this.shippingPackages = shippingPackages;
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
