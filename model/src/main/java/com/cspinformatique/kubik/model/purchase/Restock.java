package com.cspinformatique.kubik.model.purchase;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.cspinformatique.kubik.model.product.Product;

@Entity
public class Restock {
	public enum Status {
		OPEN, VALIDATED, SKIPPED
	}

	private int id;
	private Product product;
	private double quantity;
	private Status status;
	private Date openDate;
	private Date skippedDate;
	private Date validatedDate;
	private PurchaseSession purchaseSession;

	public Restock() {

	}

	public Restock(int id, Product product, double quantity, Status status,
			Date openDate, Date skippedDate, Date validatedDate, PurchaseSession purchaseSession) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
		this.status = status;
		this.openDate = openDate;
		this.skippedDate = skippedDate;
		this.validatedDate = validatedDate;
		this.purchaseSession = purchaseSession;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToOne
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

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getSkippedDate() {
		return skippedDate;
	}

	public void setSkippedDate(Date skippedDate) {
		this.skippedDate = skippedDate;
	}

	public Date getValidatedDate() {
		return validatedDate;
	}

	public void setValidatedDate(Date validatedDate) {
		this.validatedDate = validatedDate;
	}

	@OneToOne
	public PurchaseSession getPurchaseSession() {
		return purchaseSession;
	}

	public void setPurchaseSession(PurchaseSession purchaseSession) {
		this.purchaseSession = purchaseSession;
	}
}
