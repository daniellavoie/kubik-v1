package com.cspinformatique.kubik.model.sales;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class InvoiceTaxAmount {
	private Integer id;
	private Double taxRate;
	private Double taxAmount;
	
	public InvoiceTaxAmount(){
		
	}

	public InvoiceTaxAmount(Integer id, Double taxRate, Double taxAmount) {
		this.id = id;
		this.taxRate = taxRate;
		this.taxAmount = taxAmount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
}
