package com.cspinformatique.kubik.domain.purchase.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cspinformatique.kubik.model.product.Supplier;

@Entity
public class Rma {
	public enum Status{
		
	}
	
	private Integer id;
	private Supplier supplier;
	private List<RmaDetail> details;
	private Date creationDate;
	private Date closedDate;
	
	public Rma(){
		
	}

	public Rma(Integer id, Supplier supplier, List<RmaDetail> details,
			Date creationDate, Date closedDate) {
		this.id = id;
		this.supplier = supplier;
		this.details = details;
		this.creationDate = creationDate;
		this.closedDate = closedDate;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}
}
