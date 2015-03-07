package com.cspinformatique.kubik.sales.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PaymentMethod {
	public enum Types{
		CASH, CARD, CHECK, CREDIT
	}
	
	private String type;
	private String description;
	
	public PaymentMethod(){
		
	}
	
	public PaymentMethod(String type, String description){
		this.type = type;
		this.description = description;
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
}
