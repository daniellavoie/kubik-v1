package com.cspinformatique.kubik.purchase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.product.model.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class ReceptionDetail {
	private Integer id;
	private Reception reception;
	private Product product;
	private double quantityToReceive;
	private double quantityReceived;
	
	public ReceptionDetail(){
		
	}

	public ReceptionDetail(Integer id, Reception reception, Product product,
			double quantityToReceive, double quantityReceived) {
		this.id = id;
		this.reception = reception;
		this.product = product;
		this.quantityToReceive = quantityToReceive;
		this.quantityReceived = quantityReceived;
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
}
