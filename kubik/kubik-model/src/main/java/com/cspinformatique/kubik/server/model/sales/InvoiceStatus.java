package com.cspinformatique.kubik.server.model.sales;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InvoiceStatus {
	public enum Types{
		DRAFT, ORDER, ORDER_CONFIRMED, CANCELED, PAID, REFUND
	}
	
	private String type;
	private String description;
	
	public InvoiceStatus(){
		
	}

	public InvoiceStatus(String type, String description) {
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
