package com.cspinformatique.kubik.domain.purchase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import com.cspinformatique.kubik.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PurchaseOrderDetail {
	private Integer id;
	private PurchaseOrder purchaseOrder;
	private Product product;
	private double quantity;

	private float discount;
	private float discountApplied;
	private DiscountType discountType;
	private double unitPriceTaxOut;
	private double totalAmountTaxOut;

	public PurchaseOrderDetail() {

	}

	public PurchaseOrderDetail(Integer id, PurchaseOrder purchaseOrder,
			Product product, double quantity, float discount,
			float discountApplied, DiscountType discountType,
			double unitPriceTaxOut, double totalAmountTaxOut) {
		this.id = id;
		this.purchaseOrder = purchaseOrder;
		this.product = product;
		this.quantity = quantity;
		this.discount = discount;
		this.discountApplied = discountApplied;
		this.discountType = discountType;
		this.unitPriceTaxOut = unitPriceTaxOut;
		this.totalAmountTaxOut = totalAmountTaxOut;
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
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getDiscountApplied() {
		return discountApplied;
	}

	public void setDiscountApplied(float discountApplied) {
		this.discountApplied = discountApplied;
	}

	@ManyToOne
	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public double getUnitPriceTaxOut() {
		return unitPriceTaxOut;
	}

	public void setUnitPriceTaxOut(double unitPriceTaxOut) {
		this.unitPriceTaxOut = unitPriceTaxOut;
	}

	public double getTotalAmountTaxOut() {
		return totalAmountTaxOut;
	}

	public void setTotalAmountTaxOut(double totalAmountTaxOut) {
		this.totalAmountTaxOut = totalAmountTaxOut;
	}
}
