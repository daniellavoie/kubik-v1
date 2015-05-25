package com.cspinformatique.kubik.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum DeliveryDateType {
	SOONEST("010", "Livraison au plus tôt"),
	LATEST("010", "Livraison au plus tard"),
	FORECAST("012", "Prévisionnelle"),
	PERIOD("013", "Période"),
	SHIPPING("014", "Expédition"),
	PLANNING("030", "Préparation"),
	AVAILABLE("031", "Mise à disposition");
	
	private String type;
	private String description;
	
	DeliveryDateType(String type, String description){
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static DeliveryDateType parseByType(String type){
		for(DeliveryDateType deliveryDateType : DeliveryDateType.values()){
			if(deliveryDateType.getType().equals(type)){
				return deliveryDateType;
			}
		}
		
		throw new IllegalArgumentException("No enum with type " + type);
	}
}
