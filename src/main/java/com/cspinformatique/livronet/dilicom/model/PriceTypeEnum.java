package com.cspinformatique.livronet.dilicom.model;

public enum PriceTypeEnum {
	TAX_IN(4, "Prix de vente TTC"),
	EXPORT(5, "Prix export"),
	YIELD(6, "Prix de cession"),
	FOREIGN_PUBLISHER_CATALOG(9, "Prix catelogue éditeur étranger");
	
	private int code;
	private String description;
	
	PriceTypeEnum(int code, String description){
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString(){
		return this.description;
	}
}
