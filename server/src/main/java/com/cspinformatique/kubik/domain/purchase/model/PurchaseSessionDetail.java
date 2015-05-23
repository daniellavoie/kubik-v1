package com.cspinformatique.kubik.domain.purchase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class PurchaseSessionDetail {
	private int id;
	private PurchaseSession purchaseSession;
	private Product product;
	private double quantity;
	
	public PurchaseSessionDetail(){
		
	}

	public PurchaseSessionDetail(int id, PurchaseSession purchaseSession,
			Product product, double quantity) {
		this.id = id;
		this.purchaseSession = purchaseSession;
		this.product = product;
		this.quantity = quantity;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	public PurchaseSession getPurchaseSession() {
		return purchaseSession;
	}

	public void setPurchaseSession(PurchaseSession purchaseSession) {
		this.purchaseSession = purchaseSession;
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
