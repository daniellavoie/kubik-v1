package com.cspinformatique.kubik.server.domain.dilicom.batch.mapper;

public enum ReferenceDeletedMappingEnum {
	MOVEMENT_CODE("movementCode", 1, 1),
	EAN13("ean13", 2, 14),
	DISTRIBUTOR_EAN13("distributorEan13", 15, 27),	
	BLANK("distributorEan13", 28, 401);	
	
	private String fieldName;
	private int startRange;
	private int endRange;
	
	ReferenceDeletedMappingEnum(String fieldName, int startRange, int endRange){
		this.fieldName = fieldName;
		this.startRange = startRange;
		this.endRange = endRange;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getStartRange() {
		return startRange;
	}

	public void setStartRange(int startRange) {
		this.startRange = startRange;
	}

	public int getEndRange() {
		return endRange;
	}

	public void setEndRange(int endRange) {
		this.endRange = endRange;
	}
}
