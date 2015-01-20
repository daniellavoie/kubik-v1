package com.cspinformatique.kubik.batch.mapper;

public enum ReferenceExtractionMappingEnum {
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
	AVAILABLEVFOR_ORDER("availableForOrder", 85, 85),	
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
	REFERENCESCOUNT("referencesCount", 398, 401),
	
	DILICOM_CREATION_DATE("dilicomCreationDate", 402, 409),
	DILICOM_MODIFICATION_DATE("dilicomModificationDate", 410, 417),
	FUTURE_PRICE_APPLICATION_OR_AVAILABILITY_DATE("futurePriceApplicationOrAvailabilityDate", 418, 425),
	FUTURE_AVAILABILITY("futureAvailability", 426, 426),
	FUTURE_PRICE_TYPE("futurePriceType", 427, 427),
	FUTURE_PRICE_TAX_IN("futurePriceTaxIn", 428, 435),
	FUTURE_SCHOOLBOOK("futureSchoolbook", 436, 437),
	FUTURE_TVA_RATE_1("futureTvaRate1", 438, 441),
	FUTURE_PRICE_TAX_OUT_1("futurePriceTaxOut1", 442 , 449),
	FUTURE_TVA_RATE_2("futureTvaRate2", 450, 453),
	FUTURE_PRICE_TAX_OUT_2("futurePriceTaxOut2", 454, 461),
	FUTURE_TVA_RATE3("futureTvaRate3", 462, 465),
	FUTURE_PRICE_TAX_OUT_3("futurePriceTaxOut3", 466, 473),
	FUTURE_RETURN_TYPE("futureReturnType", 474, 474),
	FUTURE_AVAILABLE_FOR_ORDER("futureAvailableForOrder", 475, 475);
	
	private String fieldName;
	private int startRange;
	private int endRange;
	
	ReferenceExtractionMappingEnum(String fieldName, int startRange, int endRange){
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
