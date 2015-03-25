package com.cspinformatique.kubik.domain.purchase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum NotationCode {
	USUAL_RULE(0, "Règle habituelle"),
	NOTE_IF_MISSING(1, "Noter si manquant"),
	DO_NOT_NOTE_IF_MISSING(3, "Ne pas noter si manquant"),
	NOTE_IF_NEW(4, "Noter si nouveauté");
	
	private int code;
	private String description;
	
	NotationCode(int code, String description){
		this.code = code;
		this.description = description;
	}

    
	public int getCode() {
		return code;
	}
	
	@JsonValue
	public String getDescription() {
		return description;
	}
}
