package com.cspinformatique.kubik.model.broadleaf;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.model.product.Product;

@Entity
public class BroadleafNotification {
	public enum Status {
		TO_PROCESS, PROCESSED, ERROR
	}

	private int id;
	private Product product;
	private Status status;
	private Date creationDate;
	private Date processedDate;
	private Date errorDate;
	private String error;

	public BroadleafNotification() {

	}

	public BroadleafNotification(int id, Product product, Status status,
			Date creationDate, Date processedDate, Date errorDate, String error) {
		this.id = id;
		this.product = product;
		this.status = status;
		this.creationDate = creationDate;
		this.processedDate = processedDate;
		this.errorDate = errorDate;
		this.error = error;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public Date getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}

	@Lob
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
