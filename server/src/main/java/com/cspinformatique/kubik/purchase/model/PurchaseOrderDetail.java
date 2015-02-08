package com.cspinformatique.kubik.purchase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.product.model.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class PurchaseOrderDetail {
	private Integer id;
	private PurchaseOrder purchaseOrder;
	private Product product;
	private double quantity;
	private double discount;
	private double unitPrice;
	private double totalPrice;
	
	public PurchaseOrderDetail(){
		
	}

	public PurchaseOrderDetail(Integer id, PurchaseOrder purchaseOrder,
			Product product, double quantity, double discount, double unitPrice, double totalPrice) {
		this.id = id;
		this.purchaseOrder = purchaseOrder;
		this.product = product;
		this.quantity = quantity;
		this.discount = discount;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
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

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
