package com.cspinformatique.kubik.model.sales;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.cspinformatique.kubik.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class CustomerCreditDetail {
	private Integer id;
	private CustomerCredit customerCredit;
	private Product product;
	private double quantity;
	private double maxQuantity;
	private Double unitPrice;
	private Double totalAmount;

	public CustomerCreditDetail() {

	}

	public CustomerCreditDetail(Integer id, CustomerCredit customerCredit, Product product, double quantity,
			double maxQuantity, Double unitPrice, Double totalAmount) {
		this.id = id;
		this.customerCredit = customerCredit;
		this.product = product;
		this.quantity = quantity;
		this.maxQuantity = maxQuantity;
		this.unitPrice = unitPrice;
		this.totalAmount = totalAmount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	public CustomerCredit getCustomerCredit() {
		return customerCredit;
	}

	public void setCustomerCredit(CustomerCredit customerCredit) {
		this.customerCredit = customerCredit;
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

	public double getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(double maxQuantity) {
		this.maxQuantity = maxQuantity;
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
