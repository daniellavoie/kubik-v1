package com.cspinformatique.kubik.model.purchase;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DiscountType {
	public enum Types{
		SUPPLIER, PRODUCT, ORDER, ORDER_DETAIL
	}
	
	private String type;
	private String description;
	
	public DiscountType() {

	}

	public DiscountType(String type, String description) {
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
