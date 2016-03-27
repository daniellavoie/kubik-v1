package com.cspinformatique.kubik.server.model.warehouse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.server.model.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class StocktakingDiff {
	private long id;
	private Product product;
	private Stocktaking stocktaking;
	private double adjustmentQuantity;
	private double countedQuantity;
	private boolean validated;

	public StocktakingDiff() {

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
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	@JsonBackReference
	public Stocktaking getStocktaking() {
		return stocktaking;
	}

	public void setStocktaking(Stocktaking stocktaking) {
		this.stocktaking = stocktaking;
	}

	public double getAdjustmentQuantity() {
		return adjustmentQuantity;
	}

	public void setAdjustmentQuantity(double adjustmentQuantity) {
		this.adjustmentQuantity = adjustmentQuantity;
	}

	public double getCountedQuantity() {
		return countedQuantity;
	}

	public void setCountedQuantity(double countedQuantity) {
		this.countedQuantity = countedQuantity;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}
}
