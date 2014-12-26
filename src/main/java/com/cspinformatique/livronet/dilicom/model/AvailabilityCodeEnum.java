package com.cspinformatique.livronet.dilicom.model;

public enum AvailabilityCodeEnum {
	AVAILABLE("01", "Disponible"),
	NOT_PUBLISHED_YET("02", "Pas encore paru"),
	REPRINT_IN_PROGRESS("03", "Réimpression en cours"),
	TEMPORARY_NOT_AVAILABLE("04", "Non disponible provisoirement"),
	NOT_DISTRIBUTED_ANYMORE("05", "Ne sera plus distribué par nous"),
	NOT_PUBLISHED_ANYMORE("06", "Arrêt définitif de commercialisation"),
	UNDEFINED_BREAK("07", "Manque sans date"),
	TO_BE_REPUBLISHED("08", "À reparaître"),
	PUBLISH_CANCELED("09", "Abandon de parution");
	
	private String code;
	private String description;
	
	AvailabilityCodeEnum(String code, String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
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
