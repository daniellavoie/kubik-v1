package com.cspinformatique.kubik.server.model.purchase;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cspinformatique.kubik.server.model.product.Supplier;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Rma {
	public enum Status{
		OPEN, CANCELED, SHIPPED
	}
	
	private Integer id;
	private Supplier supplier;
	private List<RmaDetail> details;
	private Status status;
	private Date openDate;
	private Date canceledDate;
	private Date shippedDate;
	
	public Rma(){
		
	}

	public Rma(Integer id, Supplier supplier, List<RmaDetail> details,
			Status status, Date openDate, Date canceledDate, Date shippedDate) {
		this.id = id;
		this.supplier = supplier;
		this.details = details;
		this.status = status;
		this.openDate = openDate;
		this.canceledDate = canceledDate;
		this.shippedDate = shippedDate;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	public List<RmaDetail> getDetails() {
		return details;
	}

	public void setDetails(List<RmaDetail> details) {
		this.details = details;
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

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

	public Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}
}
