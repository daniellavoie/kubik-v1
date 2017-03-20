package com.daniellavoie.kubik.product.vehicule.model;

import java.util.List;

public class Product {
	public enum Packaging {
		STANDARD, PACK, KIT
	}

	public enum SupplierType {
		NONE, SIFAM
	}

	private int id;
	private int categoryId;
	private int supplierId;
	private String ean13;
	private List<SupplierReference> supplierReferences;
	private String brand;
	private String name;
	private String categoryName;
	private double publicPrice;
	private double sellingPrice;
	private double buyingPrice;
	private double weight;
	private String description;
	private String caracteristics;
	private Packaging packaging;

	public Product() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public List<SupplierReference> getSupplierReferences() {
		return supplierReferences;
	}

	public void setSupplierReferences(List<SupplierReference> supplierReferences) {
		this.supplierReferences = supplierReferences;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(double publicPrice) {
		this.publicPrice = publicPrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public double getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCaracteristics() {
		return caracteristics;
	}

	public void setCaracteristics(String caracteristics) {
		this.caracteristics = caracteristics;
	}

	public Packaging getPackaging() {
		return packaging;
	}

	public void setPackaging(Packaging packaging) {
		this.packaging = packaging;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", ean13=" + ean13 + ", brand=" + brand + ", name=" + name + "]";
	}
}
