package com.cspinformatique.kubik.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum PriceType {
	TAX_IN(4, "Prix de vente TTC"),
	EXPORT(5, "Prix export"),
	YIELD(6, "Prix de cession"),
	FOREIGN_PUBLISHER_CATALOG(9, "Prix catelogue éditeur étranger");
	
	private int code;
	private String description;
	
	PriceType(int code, String description){
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
	
	public static PriceType parseByCode(int code){
		for(PriceType priceType : PriceType.values()){
			if(priceType.getCode() == code){
				return priceType;
			}
		}
		
		throw new IllegalArgumentException("No enum with code " + code);
	}
}
