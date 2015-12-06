package com.cspinformatique.kubik.server.domain.dilicom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.cspinformatique.kubik.server.model.product.Product;

@Entity
public class ReferenceNotification {
	public enum Status {NEW, PROCESSED};
	
	private Integer id;
	private Product product;
	private Status status;
	private Date creationDate;
	private Date processedDate;
	
	public ReferenceNotification(){
		
	}

	public ReferenceNotification(Integer id, Product product, Status status,
			Date creationDate, Date processedDate) {
		this.id = id;
		this.product = product;
		this.status = status;
		this.creationDate = creationDate;
		this.processedDate = processedDate;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne
	@JoinColumn(unique=true)
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
}
