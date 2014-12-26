package com.cspinformatique.livronet.dilicom.model;

public enum StoreDisplayType {
	LINEAR("01", "Posé en linéaire"),
	SPINDLE("02", "Sur broche"),
	STAND("03", "Présentoir"),
	PALET("05", "En palette présentoir (posé au sol)");
	
	private String code;
	private String description;
	
	StoreDisplayType(String code, String description){
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
