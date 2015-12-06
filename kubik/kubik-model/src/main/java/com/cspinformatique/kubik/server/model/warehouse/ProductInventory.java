package com.cspinformatique.kubik.server.model.warehouse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.cspinformatique.kubik.server.model.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class ProductInventory {
	private Integer id;
	private Product product;
	private double quantityOnHand;
	private double quantityOnHold;
	
	public ProductInventory(){
		
	}

	public ProductInventory(Integer id, Product product, double quantityOnHand,
			double quantityOnHold) {
		this.id = id;
		this.product = product;
		this.quantityOnHand = quantityOnHand;
		this.quantityOnHold = quantityOnHold;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne
	@JsonBackReference
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getQuantityOnHand() {
		return quantityOnHand;
	}

	public void setQuantityOnHand(double quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}

	public double getQuantityOnHold() {
		return quantityOnHold;
	}

	public void setQuantityOnHold(double quantityOnHold) {
		this.quantityOnHold = quantityOnHold;
	}
}
