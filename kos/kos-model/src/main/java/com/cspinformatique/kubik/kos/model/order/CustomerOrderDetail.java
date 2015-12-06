package com.cspinformatique.kubik.kos.model.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.kos.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CustomerOrderDetail {
	private long id;
	private CustomerOrder customerOrder;
	private Product product;
	private int quantity;
	private Double unitPrice;
	private Double totalAmount;

	public CustomerOrderDetail() {

	}

	public CustomerOrderDetail(long id, CustomerOrder customerOrder, Product product, int quantity, Double unitPrice, Double totalAmount) {
		this.id = id;
		this.customerOrder = customerOrder;
		this.product = product;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalAmount = totalAmount;
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
	@JsonIgnore
	public CustomerOrder getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
	}

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
