package com.daniellavoie.kubik.product.vehicule.model;

import com.daniellavoie.kubik.product.vehicule.model.Supplier.Type;

public class SupplierReference {
	private Type supplierType;
	private String reference;

	public Type getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(Type supplierType) {
		this.supplierType = supplierType;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
