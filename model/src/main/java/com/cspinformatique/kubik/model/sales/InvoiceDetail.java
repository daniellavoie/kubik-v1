package com.cspinformatique.kubik.model.sales;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cspinformatique.kubik.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class InvoiceDetail {
	private Integer id;
	private Invoice invoice;
	private Product product;
	private Double quantity;
	private Map<Double, InvoiceTaxAmount> taxesAmounts;
	private Double unitPrice;
	private Double totalAmount;
	private Double totalTaxAmount;
	private Double totalTaxLessAmount;

	public InvoiceDetail() {

	}

	public InvoiceDetail(Integer id, Invoice invoice, Product product,
			Double quantity, Map<Double, InvoiceTaxAmount> taxesAmounts,
			Double unitPrice, Double totalAmount, Double totalTaxAmount,
			Double totalTaxLessAmount) {
		this.id = id;
		this.invoice = invoice;
		this.product = product;
		this.quantity = quantity;
		this.taxesAmounts = taxesAmounts;
		this.unitPrice = unitPrice;
		this.totalAmount = totalAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalTaxLessAmount = totalTaxLessAmount;
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
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	public Map<Double, InvoiceTaxAmount> getTaxesAmounts() {
		return taxesAmounts;
	}

	public void setTaxesAmounts(Map<Double, InvoiceTaxAmount> taxesAmounts) {
		this.taxesAmounts = taxesAmounts;
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

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getTotalTaxLessAmount() {
		return totalTaxLessAmount;
	}

	public void setTotalTaxLessAmount(Double totalSubAmount) {
		this.totalTaxLessAmount = totalSubAmount;
	}
}
