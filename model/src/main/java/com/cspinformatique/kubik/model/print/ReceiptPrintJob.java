package com.cspinformatique.kubik.model.print;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cspinformatique.kubik.model.sales.Invoice;

@Entity
public class ReceiptPrintJob {
	private Integer id;
	private Invoice invoice;
	private Date startDate;
	
	public ReceiptPrintJob(){
		
	}

	public ReceiptPrintJob(Integer id, Invoice invoice, Date startDate) {
		this.id = id;
		this.invoice = invoice;
		this.startDate = startDate;
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
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
