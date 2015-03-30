package com.cspinformatique.kubik.domain.purchase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import com.cspinformatique.kubik.model.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Audited
public class ReceptionDetail {
	private Integer id;
	private Reception reception;
	private Product product;
	private double quantityToReceive;
	private double quantityReceived;

	private float discount;
	private float discountApplied;
	private DiscountType discountType;
	private double unitPriceTaxOut;
	private double totalAmountTaxOut;
	
	public ReceptionDetail(){
		
	}

	public ReceptionDetail(Integer id, Reception reception, Product product,
			double quantityToReceive, double quantityReceived, float discount,
			float discountApplied, DiscountType discountType,
			double unitPriceTaxOut, double totalAmountTaxOut) {
		this.id = id;
		this.reception = reception;
		this.product = product;
		this.quantityToReceive = quantityToReceive;
		this.quantityReceived = quantityReceived;
		this.discount = discount;
		this.discountApplied = discountApplied;
		this.discountType = discountType;
		this.unitPriceTaxOut = unitPriceTaxOut;
		this.totalAmountTaxOut = totalAmountTaxOut;
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
	@JsonBackReference
	public Reception getReception() {
		return reception;
	}

	public void setReception(Reception reception) {
		this.reception = reception;
	}

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getQuantityToReceive() {
		return quantityToReceive;
	}

	public void setQuantityToReceive(double quantityToReceive) {
		this.quantityToReceive = quantityToReceive;
	}

	public double getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(double quantityReceived) {
		this.quantityReceived = quantityReceived;
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
