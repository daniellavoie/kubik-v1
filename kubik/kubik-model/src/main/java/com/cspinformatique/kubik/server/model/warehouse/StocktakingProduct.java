package com.cspinformatique.kubik.server.model.warehouse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.server.model.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class StocktakingProduct {
	private long id;
	private StocktakingCategory category;
	private Product product;
	private double quantity;
	private double inventoryQuantity;

	public StocktakingProduct() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JsonBackReference
	public StocktakingCategory getCategory() {
		return category;
	}

	public void setCategory(StocktakingCategory category) {
		this.category = category;
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

	public double getInventoryQuantity() {
		return inventoryQuantity;
	}

	public void setInventoryQuantity(double inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
}
