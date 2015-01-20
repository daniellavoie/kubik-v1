package com.cspinformatique.kubik.purchase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum ShippingMode {
	USUAL_SHIPPING_LOCATION("01", "Mise à disposition au lieu habituel du distributeur"),
	EXPRESS_SHIPPING("02", "Colis express"),
	MAIL("03", "Poste"),
	PRISME("04", "Prisme"),
	SNCF("05", "SNCF"),
	PLANE("06", "Avion"),
	MESSENGER("07", "Coursier du libraire"),
	DELIVERY("08", "A livrer"),
	USUAL_METHOD("09", "Habituel");
	
	private String code;
	private String description;
	
    
	ShippingMode(String code, String description){
		this.code = code;
		this.description = description;
	}
    
	public String getCode() {
		return code;
	}
	
    @JsonValue
	public String getDescription() {
		return description;
	}
	
	
}
