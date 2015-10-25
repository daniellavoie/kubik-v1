package com.cspinformatique.kubik.model.warehouse;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.model.product.Product;

@Entity
public class InventoryCount {
	private long id;
	private Product product;
	private double quantity;
	private Date dateCounted;
	private String reason;
	
	public InventoryCount(){
		
	}
	
	public InventoryCount(long id, Product product, double quantity, Date dateCounted, String reason) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
		this.dateCounted = dateCounted;
		this.reason = reason;
	}

	@Id
	@GeneratedValue
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

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Date getDateCounted() {
		return dateCounted;
	}

	public void setDateCounted(Date dateCounted) {
		this.dateCounted = dateCounted;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
