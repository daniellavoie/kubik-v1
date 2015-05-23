package com.cspinformatique.kubik.domain.purchase.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class PurchaseSession {
	public enum Status {
		DRAFT, CANCELED, SUBMITED;
	}

	private Integer id;
	private Date minDeliveryDate;
	private Date maxDeliveryDate;
	private List<PurchaseSessionDetail> details;
	private Status status;
	private Date openDate;
	private Date closeDate;

	private List<PurchaseOrder> purchaseOrders;

	public PurchaseSession() {

	}

	public PurchaseSession(Integer id, Date minDeliveryDate,
			Date maxDeliveryDate, List<PurchaseSessionDetail> details,
			Status status, Date openDate, Date closeDate,
			List<PurchaseOrder> purchaseOrders) {
		this.id = id;
		this.minDeliveryDate = minDeliveryDate;
		this.maxDeliveryDate = maxDeliveryDate;
		this.details = details;
		this.status = status;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.purchaseOrders = purchaseOrders;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getMinDeliveryDate() {
		return minDeliveryDate;
	}

	public void setMinDeliveryDate(Date minDeliveryDate) {
		this.minDeliveryDate = minDeliveryDate;
	}

	public Date getMaxDeliveryDate() {
		return maxDeliveryDate;
	}

	public void setMaxDeliveryDate(Date maxDeliveryDate) {
		this.maxDeliveryDate = maxDeliveryDate;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PurchaseSessionDetail> getDetails() {
		return details;
	}

	public void setDetails(List<PurchaseSessionDetail> details) {
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

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
}
