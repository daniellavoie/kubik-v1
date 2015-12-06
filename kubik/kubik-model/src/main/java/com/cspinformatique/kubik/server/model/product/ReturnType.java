package com.cspinformatique.kubik.server.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum ReturnType {
	CONDITIONNAL(1, "Retour possible selon les CGV (conditions générales de vente)"),
	FORBIDEN_CLOSED_ACCOUNT(2, "Retour interdit (compte ferme)"),
	FORBIDEN_EXCEPT_AGREMENT(3, "Retour interdit sauf accord commercial particulier"),
	ON_COVERAGE(5, "Retour sur couverture possible (selon les CGV)"),
	ON_COVERAGE_ONLY(6, "Retour sur couverture obligatoire [en cas de retour celui-ci ne sera possible que sur couverture]");
	
	private int code;
	private String description;
	
	ReturnType(int code, String description){
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
	
	public static ReturnType parseByCode(int code){		
		for(ReturnType returnType : ReturnType.values()){
			if(returnType.getCode() == code){
				return returnType;
			}
		}
		
		throw new IllegalArgumentException("No enum with code " + code);
	}	
}
