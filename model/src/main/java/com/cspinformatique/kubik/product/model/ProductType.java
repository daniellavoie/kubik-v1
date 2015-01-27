package com.cspinformatique.kubik.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum ProductType {
	CONSUMABLE_UNIT("01", "Unité Consommateur"),
	CONSUMABLE_LOT("02", "Lot consommateur"),
	PROFESSIONAL_LOT("03", " Lot professionnel"),
	DISPLAY_UNIT("04", "Présentoir"),
	PROFESSIONAL_CARD("09", "Fiche strictement professionnelle "),
	MARKETING_UNIT("15", "Publicité sur le Lieu de Vente");
	
	private String code;
	private String description;
	
	ProductType(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString(){
		return this.description;
	}
	
	public static ProductType parseByCode(String code){
		for(ProductType productType : ProductType.values()){
			if(productType.getCode().equals(code)){
				return productType;
			}
		}
		
		throw null;
	}
}
