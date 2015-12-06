package com.cspinformatique.kubik.server.model.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Supplier {
	private Integer id;
	private String ean13;
	private String name;
	private String purchaseOrderEan13;
	private float discount;
	private String accountNumber;
	private String address;
	
	public Supplier(){
		
	}

	public Supplier(Integer id, String ean13, String name, String purchaseOrderEan13, float discount, String accountNumber, String address) {
		this.id = id;
		this.ean13 = ean13;
		this.name = name;
		this.purchaseOrderEan13 = purchaseOrderEan13;
		this.discount = discount;
		this.accountNumber = accountNumber;
		this.address = address;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPurchaseOrderEan13() {
		return purchaseOrderEan13;
	}

	public void setPurchaseOrderEan13(String purchaseOrderEan13) {
		this.purchaseOrderEan13 = purchaseOrderEan13;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
