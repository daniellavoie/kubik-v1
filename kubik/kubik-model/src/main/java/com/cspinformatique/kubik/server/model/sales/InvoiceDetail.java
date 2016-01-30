package com.cspinformatique.kubik.server.model.sales;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.cspinformatique.kubik.server.model.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
public class InvoiceDetail {
	private Integer id;
	private Invoice invoice;
	private Product product;
	private Double quantity;
	private InvoiceTaxAmount taxAmount;
	private Double unitPrice;
	private Double totalAmount;
	private Double totalTaxableAmount;
	private Double totalTaxAmount;
	private Double totalTaxLessAmount;
	private Double totalRebateAmount;

	public InvoiceDetail() {

	}

	public InvoiceDetail(Integer id, Invoice invoice, Product product, Double quantity, Double totalTaxableAmount,
			InvoiceTaxAmount taxAmount, Double unitPrice, Double totalAmount, Double totalTaxAmount,
			Double totalTaxLessAmount, Double totalRebateAmount) {
		this.id = id;
		this.invoice = invoice;
		this.product = product;
		this.quantity = quantity;
		this.totalTaxableAmount = totalTaxableAmount;
		this.taxAmount = taxAmount;
		this.unitPrice = unitPrice;
		this.totalAmount = totalAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalTaxLessAmount = totalTaxLessAmount;
		this.totalRebateAmount = totalRebateAmount;
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
	@JsonBackReference
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@ManyToOne
	@NotAudited
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

	public Double getTotalTaxableAmount() {
		return totalTaxableAmount;
	}

	public void setTotalTaxableAmount(Double totalTaxableAmount) {
		this.totalTaxableAmount = totalTaxableAmount;
	}

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	public InvoiceTaxAmount getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(InvoiceTaxAmount taxAmount) {
		this.taxAmount = taxAmount;
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

	public Double getTotalRebateAmount() {
		return totalRebateAmount;
	}

	public void setTotalRebateAmount(Double totalRebateAmount) {
		this.totalRebateAmount = totalRebateAmount;
	}
}
