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
	private int quantityOrdered;
	private int quantityShipped;
	private Double unitPrice;
	private Double totalAmount;

	public CustomerOrderDetail() {

	}

	public CustomerOrderDetail(long id, CustomerOrder customerOrder, Product product, int quantityOrdered, int quantityShipped, Double unitPrice, Double totalAmount) {
		this.id = id;
		this.customerOrder = customerOrder;
		this.product = product;
		this.quantityOrdered = quantityOrdered;
		this.quantityShipped = quantityShipped;
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

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public int getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(int quantityShipped) {
		this.quantityShipped = quantityShipped;
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
