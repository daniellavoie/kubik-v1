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
	
	public PurchaseOrderDetail(){
		
	}

	public PurchaseOrderDetail(Integer id, PurchaseOrder purchaseOrder,
			Product product, double quantity) {
		this.id = id;
		this.purchaseOrder = purchaseOrder;
		this.product = product;
		this.quantity = quantity;
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
}
