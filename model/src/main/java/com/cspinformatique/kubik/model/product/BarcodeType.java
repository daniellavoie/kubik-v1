package com.cspinformatique.kubik.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum BarcodeType {
	WITH_BARCODE(1, "Avec code à barres"),
	WITHOUT_BARCODE(2, "Sans code à barres"),
	WITH_RF(2, "RF avec code à barres"),
	WITHOUT_RF(3, "RF sans code à barres"),
	WITH_RFID(4, "Avec RFID"),
	WITHOUT_RFID(5, "Sans RFID");
	
	private int code;
	private String description;
	
	BarcodeType(int code, String description){
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
	
	public static BarcodeType parseByCode(int code){
		for(BarcodeType barcodeType : BarcodeType.values()){
			if(barcodeType.getCode() == code){
				return barcodeType;
			}
		}
		
		throw new IllegalArgumentException("No enum with code " + code);
	}
}