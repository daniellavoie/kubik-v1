package com.cspinformatique.kubik.domain.dilicom.batch.mapper;

public enum ReferenceNotificationMappingEnum {
	MOVEMENT_CODE("movementCode", 1, 1),
	EAN13("ean13", 2, 14),
	DISTRIBUTOR_EAN13("distributorEan13", 15, 27),
	PRICE_APPLICATION_OR_AVAILABILITY_DATE("priceApplicationOrAvailabilityDate", 28, 35),
	AVAILABILITY("availability", 36, 36),
	PRICE_TYPE("priceType", 37, 37),
	PRICE_TAX_IN("priceTaxIn", 38, 45),
	SCHOOLBOOK("schoolbook", 46, 47),
	TVA_RATE_1("tvaRate1", 48, 51),
	PRICE_TAX_OUT_1("priceTaxOut1", 52 ,59),
	TVA_RATE_2("tvaRate2", 60, 63),
	PRICE_TAX_OUT_2("priceTaxOut2", 64, 71),
	TVA_RATE3("tvaRate3", 72, 75),
	PRICE_TAX_OUT_3("priceTaxOut3", 76, 83),
	RETURN_TYPE("returnType", 84, 84),
	AVAILABLE_FOR_ORDER("availableForOrder", 85, 85),	
	DATE_PUBLISHED("datePublished", 95, 102),
	PRODUCT_TYPE("productType", 103, 104),
	PUBLISH_END_DATE("publishEndDate", 105, 112),
	STANDARD_LABEL("standardLabel", 113, 142),
	CASH_REGISTER_LABEL("cashRegisterLabel", 143, 162),
	THICKNESS("thickness", 165, 168),
	WIDTH("width", 169, 172),
	HEIGHT("height", 173, 176),
	WEIGHT("weight", 177, 183),
	EXTENDED_LABEL("extendedLabel", 184, 283),
	PUBLISHER("publisher", 284, 298),
	COLLECTION("collection", 299, 313),
	AUTHOR("author", 314, 333),
	PUBLISHER_PRESENTATION("publisherPresentation", 334, 335),
	ISBN("isbn", 336, 345),
	SUPPLIER_REFERENCE("supplierReference", 346, 357),
	COLLECTION_REFERENCE("collectionReference", 358, 367),
	THEME("theme", 368, 371),
	PUBLISHER_ISNB("publisherIsnb", 372, 379),
	REFERENCE_LINK_TYPE("referenceLinkType", 380, 380),
	LINKED_REFERENCE_EAN13("linkedReferenceEan13", 381, 393),
	ORDERABLE_BY_UNIT("orderableByUnit", 394, 394),
	SELL_BY_MIXED_PACKAGED("sellByMixedPackaged", 395, 395),
	BARCODE_TYPE("barcodeType", 396, 396),
	PERISHABLE("perishable", 397, 397),
	REFERENCESCOUNT("referencesCount", 398, 401);
	
	private String fieldName;
	private int startRange;
	private int endRange;
	
	ReferenceNotificationMappingEnum(String fieldName, int startRange, int endRange){
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
