package com.cspinformatique.livronet.dilicom.model;

public class DilicomReference {
	private String movementCode;
	private String ean13;
	private String distributorEan13;
	private String priceApplicationOrAvailabilityDate;
	private String availability;
	private String priceType;
	private String priceTaxIn;
	private Boolean schoolbook;
	private String tvaRate1;
	private String priceTaxOut1;
	private String tvaRate2;
	private String priceTaxOut2;
	private String tvaRate3;
	private String priceTaxOut3;
	private String returnType;
	private Boolean availableForOrder;	
	private String datePublished;
	private String productType;
	private String publishEndDate;
	private String standardLabel;
	private String cashRegisterLabel;
	private Integer thickness;
	private Integer width;
	private Integer height;
	private Integer weight;
	private String extendedLabel;
	private String publisher;
	private String collection;
	private String author;
	private String publisherPresentation;
	private String isbn;
	private String supplierReference;
	private String collectionReference;
	private String theme;
	private String publisherIsnb;
	private String referenceLinkType;
	private String linkedReferenceEan13;
	private Boolean orderableByUnit;
	private String sellByMixedPackaged;
	private String barcodeType;
	private Integer perishable;
	private Integer referencesCount;
	
	public DilicomReference(){
		
	}
	
	public DilicomReference(String movementCode, String ean13,
			String distributorEan13, String priceApplicationOrAvailabilityDate,
			String availability, String priceType, String priceTaxIn,
			Boolean schoolbook, String tvaRate1, String priceTaxOut1,
			String tvaRate2, String priceTaxOut2, String tvaRate3,
			String priceTaxOut3, String returnType, Boolean availableForOrder,
			String datePublished, String productType, String publishEndDate,
			String standardLabel, String cashRegisterLabel, Integer thickness,
			Integer width, Integer height, Integer weight,
			String extendedLabel, String publisher, String collection,
			String author, String publisherPresentation, String isbn,
			String supplierReference, String collectionReference, String theme,
			String publisherIsnb, String referenceLinkType,
			String linkedReferenceEan13, Boolean orderableByUnit,
			String sellByMixedPackaged, String barcodeType,
			Integer perishable, Integer referencesCount) {
		this.movementCode = movementCode;
		this.ean13 = ean13;
		this.distributorEan13 = distributorEan13;
		this.priceApplicationOrAvailabilityDate = priceApplicationOrAvailabilityDate;
		this.availability = availability;
		this.priceType = priceType;
		this.priceTaxIn = priceTaxIn;
		this.schoolbook = schoolbook;
		this.tvaRate1 = tvaRate1;
		this.priceTaxOut1 = priceTaxOut1;
		this.tvaRate2 = tvaRate2;
		this.priceTaxOut2 = priceTaxOut2;
		this.tvaRate3 = tvaRate3;
		this.priceTaxOut3 = priceTaxOut3;
		this.returnType = returnType;
		this.availableForOrder = availableForOrder;
		this.datePublished = datePublished;
		this.productType = productType;
		this.publishEndDate = publishEndDate;
		this.standardLabel = standardLabel;
		this.cashRegisterLabel = cashRegisterLabel;
		this.thickness = thickness;
		this.width = width;
		this.height = height;
		this.weight = weight;
		this.extendedLabel = extendedLabel;
		this.publisher = publisher;
		this.collection = collection;
		this.author = author;
		this.publisherPresentation = publisherPresentation;
		this.isbn = isbn;
		this.supplierReference = supplierReference;
		this.collectionReference = collectionReference;
		this.theme = theme;
		this.publisherIsnb = publisherIsnb;
		this.referenceLinkType = referenceLinkType;
		this.linkedReferenceEan13 = linkedReferenceEan13;
		this.orderableByUnit = orderableByUnit;
		this.sellByMixedPackaged = sellByMixedPackaged;
		this.barcodeType = barcodeType;
		this.perishable = perishable;
		this.referencesCount = referencesCount;
	}

	public String getMovementCode() {
		return movementCode;
	}

	public void setMovementCode(String movementCode) {
		this.movementCode = movementCode;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public String getDistributorEan13() {
		return distributorEan13;
	}

	public void setDistributorEan13(String distributorEan13) {
		this.distributorEan13 = distributorEan13;
	}

	public String getPriceApplicationOrAvailabilityDate() {
		return priceApplicationOrAvailabilityDate;
	}

	public void setPriceApplicationOrAvailabilityDate(
			String priceApplicationOrAvailabilityDate) {
		this.priceApplicationOrAvailabilityDate = priceApplicationOrAvailabilityDate;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPriceTaxIn() {
		return priceTaxIn;
	}

	public void setPriceTaxIn(String priceTaxIn) {
		this.priceTaxIn = priceTaxIn;
	}

	public Boolean getSchoolbook() {
		return schoolbook;
	}

	public void setSchoolbook(Boolean schoolbook) {
		this.schoolbook = schoolbook;
	}

	public String getTvaRate1() {
		return tvaRate1;
	}

	public void setTvaRate1(String tvaRate1) {
		this.tvaRate1 = tvaRate1;
	}

	public String getPriceTaxOut1() {
		return priceTaxOut1;
	}

	public void setPriceTaxOut1(String priceTaxOut1) {
		this.priceTaxOut1 = priceTaxOut1;
	}

	public String getTvaRate2() {
		return tvaRate2;
	}

	public void setTvaRate2(String tvaRate2) {
		this.tvaRate2 = tvaRate2;
	}

	public String getPriceTaxOut2() {
		return priceTaxOut2;
	}

	public void setPriceTaxOut2(String priceTaxOut2) {
		this.priceTaxOut2 = priceTaxOut2;
	}

	public String getTvaRate3() {
		return tvaRate3;
	}

	public void setTvaRate3(String tvaRate3) {
		this.tvaRate3 = tvaRate3;
	}

	public String getPriceTaxOut3() {
		return priceTaxOut3;
	}

	public void setPriceTaxOut3(String priceTaxOut3) {
		this.priceTaxOut3 = priceTaxOut3;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Boolean getAvailableForOrder() {
		return availableForOrder;
	}

	public void setAvailableForOrder(Boolean availableForOrder) {
		this.availableForOrder = availableForOrder;
	}

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getPublishEndDate() {
		return publishEndDate;
	}

	public void setPublishEndDate(String publishEndDate) {
		this.publishEndDate = publishEndDate;
	}

	public String getStandardLabel() {
		return standardLabel;
	}

	public void setStandardLabel(String standardLabel) {
		this.standardLabel = standardLabel;
	}

	public String getCashRegisterLabel() {
		return cashRegisterLabel;
	}

	public void setCashRegisterLabel(String cashRegisterLabel) {
		this.cashRegisterLabel = cashRegisterLabel;
	}

	public Integer getThickness() {
		return thickness;
	}

	public void setThickness(Integer thickness) {
		this.thickness = thickness;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getExtendedLabel() {
		return extendedLabel;
	}

	public void setExtendedLabel(String extendedLabel) {
		this.extendedLabel = extendedLabel;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisherPresentation() {
		return publisherPresentation;
	}

	public void setPublisherPresentation(String publisherPresentation) {
		this.publisherPresentation = publisherPresentation;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getSupplierReference() {
		return supplierReference;
	}

	public void setSupplierReference(String supplierReference) {
		this.supplierReference = supplierReference;
	}

	public String getCollectionReference() {
		return collectionReference;
	}

	public void setCollectionReference(String collectionReference) {
		this.collectionReference = collectionReference;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getPublisherIsnb() {
		return publisherIsnb;
	}

	public void setPublisherIsnb(String publisherIsnb) {
		this.publisherIsnb = publisherIsnb;
	}

	public String getReferenceLinkType() {
		return referenceLinkType;
	}

	public void setReferenceLinkType(String referenceLinkType) {
		this.referenceLinkType = referenceLinkType;
	}

	public String getLinkedReferenceEan13() {
		return linkedReferenceEan13;
	}

	public void setLinkedReferenceEan13(String linkedReferenceEan13) {
		this.linkedReferenceEan13 = linkedReferenceEan13;
	}

	public Boolean getOrderableByUnit() {
		return orderableByUnit;
	}

	public void setOrderableByUnit(Boolean orderableByUnit) {
		this.orderableByUnit = orderableByUnit;
	}

	public String getSellByMixedPackaged() {
		return sellByMixedPackaged;
	}

	public void setSellByMixedPackaged(String sellByMixedPackaged) {
		this.sellByMixedPackaged = sellByMixedPackaged;
	}

	public String getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public Integer getPerishable() {
		return perishable;
	}

	public void setPerishable(Integer perishable) {
		this.perishable = perishable;
	}

	public Integer getReferencesCount() {
		return referencesCount;
	}

	public void setReferencesCount(Integer referencesCount) {
		this.referencesCount = referencesCount;
	}
}
