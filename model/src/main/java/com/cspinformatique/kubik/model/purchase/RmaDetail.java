package com.cspinformatique.kubik.model.purchase;

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
public class RmaDetail {
	private int id;
	private Rma rma;
	private Product product;
	private double quantity;
	
	public RmaDetail(){
		
	}

	public RmaDetail(int id, Rma rma, Product product, double quantity) {
		this.id = id;
		this.rma = rma;
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
	public Rma getRma() {
		return rma;
	}

	public void setRma(Rma rma) {
		this.rma = rma;
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
