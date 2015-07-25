package com.cspinformatique.kubik.model.sales;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class InvoiceStatus {
	public enum Types{
		DRAFT, CANCELED, PAID, REFUND
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
