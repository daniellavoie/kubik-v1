package com.cspinformatique.kubik.model.sales;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class SalesByPaymentMethod {
	private Integer id;
	private PaymentMethod paymentMethod;
	private int salesCount;
	private double paymentsAmount;
	
	public SalesByPaymentMethod(){
		
	}

	public SalesByPaymentMethod(Integer id, PaymentMethod paymentMethod,
			int salesCount, double paymentsAmount) {
		this.id = id;
		this.paymentMethod = paymentMethod;
		this.salesCount = salesCount;
		this.paymentsAmount = paymentsAmount;
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
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public int getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}

	public double getPaymentsAmount() {
		return paymentsAmount;
	}

	public void setPaymentsAmount(double paymentsAmount) {
		this.paymentsAmount = paymentsAmount;
	}
}
