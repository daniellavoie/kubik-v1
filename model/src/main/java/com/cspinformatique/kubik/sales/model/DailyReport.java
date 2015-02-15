package com.cspinformatique.kubik.sales.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DailyReport {
	private int id;
	private Date date;
	private List<Invoice> invoices;
	private int salesCount;
	private double salesAmountTaxIn;
	private double salesAmountTaxOut;
	private List<SalesByPaymentMethod> salesByPaymentMethods;
	
	public DailyReport(){
		
	}

	public DailyReport(int id, Date date, List<Invoice> invoices, int salesCount,
			double salesAmountTaxIn, double salesAmountTaxOut,
			List<SalesByPaymentMethod> salesByPaymentMethods) {
		this.id = id;
		this.date = date;
		this.invoices = invoices;
		this.salesCount = salesCount;
		this.salesAmountTaxIn = salesAmountTaxIn;
		this.salesAmountTaxOut = salesAmountTaxOut;
		this.salesByPaymentMethods = salesByPaymentMethods;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@OneToMany
	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public int getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}

	public double getSalesAmountTaxIn() {
		return salesAmountTaxIn;
	}

	public void setSalesAmountTaxIn(double salesAmountTaxIn) {
		this.salesAmountTaxIn = salesAmountTaxIn;
	}

	public double getSalesAmountTaxOut() {
		return salesAmountTaxOut;
	}

	public void setSalesAmountTaxOut(double salesAmountTaxOut) {
		this.salesAmountTaxOut = salesAmountTaxOut;
	}

	@OneToMany(cascade=CascadeType.ALL)
	public List<SalesByPaymentMethod> getSalesByPaymentMethods() {
		return salesByPaymentMethods;
	}

	public void setSalesByPaymentMethods(
			List<SalesByPaymentMethod> salesByPaymentMethods) {
		this.salesByPaymentMethods = salesByPaymentMethods;
	}
}
