package com.cspinformatique.kubik.server.model.sales;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class PaymentMethod {
	public enum Types{
		CASH, CARD, CHECK, CREDIT, KADEOS, LIRE, WIRE
	}
	
	private String type;
	private String description;
	private String accountingCode;
	
	public PaymentMethod(){
		
	}
	
	public PaymentMethod(String type, String description, String accountingCode){
		this.type = type;
		this.description = description;
		this.accountingCode = accountingCode;
	}

	@Id
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}
}
