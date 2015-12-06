package com.cspinformatique.kubik.server.domain.warehouse.model;

import java.math.BigDecimal;

public class InventoryExtractLine {
	private String ean13;
	private String label;
	private double quantity;
	private BigDecimal purchasePrice;
	private BigDecimal purchaseValue;
	private BigDecimal taxLessPrice;
	private BigDecimal taxLessValue;

	public InventoryExtractLine() {

	}

	public InventoryExtractLine(String ean13, String label, double quantity, BigDecimal purchasePrice, BigDecimal purchaseValue,
			BigDecimal taxLessPrice, BigDecimal taxLessValue) {
		this.ean13 = ean13;
		this.label = label;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.purchaseValue = purchaseValue;
		this.taxLessPrice = taxLessPrice;
		this.taxLessValue = taxLessValue;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public BigDecimal getPurchaseValue() {
		return purchaseValue;
	}

	public void setPurchaseValue(BigDecimal purchaseValue) {
		this.purchaseValue = purchaseValue;
	}

	public BigDecimal getTaxLessPrice() {
		return taxLessPrice;
	}

	public void setTaxLessPrice(BigDecimal taxLessPrice) {
		this.taxLessPrice = taxLessPrice;
	}

	public BigDecimal getTaxLessValue() {
		return taxLessValue;
	}

	public void setTaxLessValue(BigDecimal taxLessValue) {
		this.taxLessValue = taxLessValue;
	}
}
