package com.cspinformatique.kubik.server.model.sales;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

@Entity
public class InvoiceConfirmation {
	public enum Status {
		TO_PROCESS, PROCESSED, ERROR
	}

	private Long id;
	private Invoice invoice;
	private Status status;
	private Date createdDate;
	private Date processedDate;
	private String error;

	public InvoiceConfirmation() {

	}

	public InvoiceConfirmation(Long id, Invoice invoice, Status status, Date createdDate, Date processedDate,
			String error) {
		this.id = id;
		this.invoice = invoice;
		this.status = status;
		this.createdDate = createdDate;
		this.processedDate = processedDate;
		this.error = error;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	@Lob
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
