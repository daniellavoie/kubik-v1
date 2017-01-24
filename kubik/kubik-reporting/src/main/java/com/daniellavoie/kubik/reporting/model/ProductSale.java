package com.daniellavoie.kubik.reporting.model;

import java.util.Date;
import java.util.List;

public class ProductSale {
	private long invoiceDetailId;
	private String ean13;
	private Date date;
	private String productName;
	private List<String> categories;
	private double amount;
	private double quantity;
	private double taxRate;

	public ProductSale() {

	}

	public ProductSale(long invoiceDetailId, String ean13, Date date, String productName, List<String> categories,
			double amount, double quantity, double taxRate) {
		this.invoiceDetailId = invoiceDetailId;
		this.ean13 = ean13;
		this.date = date;
		this.productName = productName;
		this.categories = categories;
		this.amount = amount;
		this.quantity = quantity;
		this.taxRate = taxRate;
	}

	public long getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(long invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
}
