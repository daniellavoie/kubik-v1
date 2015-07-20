package com.cspinformatique.kubik.model.dilicom;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class DilicomOrder {
	public enum Status {
		PENDING, TRANSFERED, PROCESSED, SHIPPED, ERROR
	}

	private int id;
	private PurchaseOrder purchaseOrder;
	private Date creationDate;
	private Date transferDate;
	private Date processDate;
	private Date validationDate;
	private Status status;
	private String remoteFilename;
	private String remoteFileContent;
	private String errorMessage;

	public DilicomOrder() {

	}

	public DilicomOrder(int id, PurchaseOrder purchaseOrder, Date creationDate,
			Date transferDate, Date processDate, Date validationDate,
			Status status, String remoteFilename, String remoteFileContent, String errorMessage) {
		this.id = id;
		this.purchaseOrder = purchaseOrder;
		this.creationDate = creationDate;
		this.transferDate = transferDate;
		this.processDate = processDate;
		this.validationDate = validationDate;
		this.status = status;
		this.remoteFilename = remoteFilename;
		this.remoteFileContent = remoteFileContent;
		this.errorMessage = errorMessage;
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
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public Date getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(Date validationDate) {
		this.validationDate = validationDate;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemoteFilename() {
		return remoteFilename;
	}

	public void setRemoteFilename(String remoteFilename) {
		this.remoteFilename = remoteFilename;
	}

	@Lob
	@Column(columnDefinition = "text")
	public String getRemoteFileContent() {
		return remoteFileContent;
	}

	public void setRemoteFileContent(String remoteFileContent) {
		this.remoteFileContent = remoteFileContent;
	}

	@Lob
	@Column(columnDefinition = "text")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
