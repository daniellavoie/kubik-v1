package com.cspinformatique.kubik.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum RelayType {
	RELAY(1, "Mise en relais"),
	CUSTOMER_PICKUP(2, "Enlèvement client");
	
	private int type;
	private String description;
	
	RelayType(int type, String description){
		this.type = type;
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
