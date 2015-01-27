package com.cspinformatique.kubik.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum AvailabilityCode {
	AVAILABLE("1", "Disponible"),
	NOT_PUBLISHED_YET("2", "Pas encore paru"),
	REPRINT_IN_PROGRESS("3", "Réimpression en cours"),
	TEMPORARY_NOT_AVAILABLE("4", "Non disponible provisoirement"),
	NOT_DISTRIBUTED_ANYMORE("5", "Ne sera plus distribué par nous"),
	NOT_PUBLISHED_ANYMORE("6", "Arrêt définitif de commercialisation"),
	UNDEFINED_BREAK("7", "Manque sans date"),
	TO_BE_REPUBLISHED("8", "À reparaître"),
	PUBLISH_CANCELED("9", "Abandon de parution");
	
	private String code;
	private String description;
	
	AvailabilityCode(String code, String description){
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
	
	public static AvailabilityCode parseByCode(String code){
		for(AvailabilityCode availabilityCode : AvailabilityCode.values()){
			if(availabilityCode.getCode().equals(code)){
				return availabilityCode;
			}
		}
		
		return null;
	}
}
