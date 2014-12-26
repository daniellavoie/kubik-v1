package com.cspinformatique.livronet.dilicom.model;

import java.util.Date;

public class Reference {
	private String ean13;
	private String distributorEan13;
	private Date priceApplicationOrAvailabilityDate;
	private AvailabilityCodeEnum availability;
	private PriceTypeEnum priceType;
	private Double priceTaxIn;
	private Boolean schoolbook;
	private Double tvaRate1;
	private Double priceTaxOut1;
	private Double tvaRate2;
	private Double priceTaxOut2;
	private Double tvaRate3;
	private Double priceTaxOut3;
	private ReturnType returnType;
	private Boolean availableForOrder;
	
	private Date datePublished;
	private ProductType productType;
	private Date publishEndDate;
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
	private PublisherPresentation publisherPresentation;
	private String isbn;
	private String supplierReference;
	private String collectionReference;
	private String theme;
	private String publisherIsnb;
	private Boolean replacesAReference;
	private Boolean replacedByAReference;
	private String replacesEan13;
	private String replacedByEan13;
	private Boolean orderableByUnit;
	private BarcodeType barcodeType;
	private Boolean mainReference;
	private Boolean secondaryReference;
	private Integer referencesCount;
	
	public Reference(){
		
	}
	
	public Reference(String ean13, String distributorEan13,
			Date priceApplicationOrAvailabilityDate,
			AvailabilityCodeEnum availability, PriceTypeEnum priceType,
			Double priceTaxIn, Boolean schoolbook, Double tvaRate1,
			Double priceTaxOut1, Double tvaRate2, Double priceTaxOut2,
			Double tvaRate3, Double priceTaxOut3, ReturnType returnType,
			Boolean availableForOrder, Date datePublished,
			ProductType productType, Date publishEndDate, String standardLabel,
			String cashRegisterLabel, Integer thickness, Integer width,
			Integer height, Integer weight, String extendedLabel, String publisher,
			String collection, String author,
			PublisherPresentation publisherPresentation, String isbn,
			String supplierReference, String collectionReference, String theme,
			String publisherIsnb, Boolean replacesAReference,
			Boolean replacedByAReference, String replacesEan13,
			String replacedByEan13, Boolean orderableByUnit,
			BarcodeType barcodeType, Boolean mainReference,
			Boolean secondaryReference, Integer referencesCount) {
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
		this.replacesAReference = replacesAReference;
		this.replacedByAReference = replacedByAReference;
		this.replacesEan13 = replacesEan13;
		this.replacedByEan13 = replacedByEan13;
		this.orderableByUnit = orderableByUnit;
		this.barcodeType = barcodeType;
		this.mainReference = mainReference;
		this.secondaryReference = secondaryReference;
		this.referencesCount = referencesCount;
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

	public Date getPriceApplicationOrAvailabilityDate() {
		return priceApplicationOrAvailabilityDate;
	}

	public void setPriceApplicationOrAvailabilityDate(
			Date priceApplicationOrAvailabilityDate) {
		this.priceApplicationOrAvailabilityDate = priceApplicationOrAvailabilityDate;
	}

	public AvailabilityCodeEnum getAvailability() {
		return availability;
	}

	public void setAvailability(AvailabilityCodeEnum availability) {
		this.availability = availability;
	}

	public PriceTypeEnum getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceTypeEnum priceType) {
		this.priceType = priceType;
	}

	public Double getPriceTaxIn() {
		return priceTaxIn;
	}

	public void setPriceTaxIn(Double priceTaxIn) {
		this.priceTaxIn = priceTaxIn;
	}

	public Boolean getSchoolbook() {
		return schoolbook;
	}

	public void setSchoolbook(Boolean schoolbook) {
		this.schoolbook = schoolbook;
	}

	public Double getTvaRate1() {
		return tvaRate1;
	}

	public void setTvaRate1(Double tvaRate1) {
		this.tvaRate1 = tvaRate1;
	}

	public Double getPriceTaxOut1() {
		return priceTaxOut1;
	}

	public void setPriceTaxOut1(Double priceTaxOut1) {
		this.priceTaxOut1 = priceTaxOut1;
	}

	public Double getTvaRate2() {
		return tvaRate2;
	}

	public void setTvaRate2(Double tvaRate2) {
		this.tvaRate2 = tvaRate2;
	}

	public Double getPriceTaxOut2() {
		return priceTaxOut2;
	}

	public void setPriceTaxOut2(Double priceTaxOut2) {
		this.priceTaxOut2 = priceTaxOut2;
	}

	public Double getTvaRate3() {
		return tvaRate3;
	}

	public void setTvaRate3(Double tvaRate3) {
		this.tvaRate3 = tvaRate3;
	}

	public Double getPriceTaxOut3() {
		return priceTaxOut3;
	}

	public void setPriceTaxOut3(Double priceTaxOut3) {
		this.priceTaxOut3 = priceTaxOut3;
	}

	public ReturnType getReturnType() {
		return returnType;
	}

	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
	}

	public Boolean getAvailableForOrder() {
		return availableForOrder;
	}

	public void setAvailableForOrder(Boolean availableForOrder) {
		this.availableForOrder = availableForOrder;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Date getPublishEndDate() {
		return publishEndDate;
	}

	public void setPublishEndDate(Date publishEndDate) {
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

	public PublisherPresentation getPublisherPresentation() {
		return publisherPresentation;
	}

	public void setPublisherPresentation(PublisherPresentation publisherPresentation) {
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

	public Boolean getReplacesAReference() {
		return replacesAReference;
	}

	public void setRemplacesAReference(Boolean replacesAReference) {
		this.replacesAReference = replacesAReference;
	}

	public Boolean getReplacedByAReference() {
		return replacedByAReference;
	}

	public void setReplacedByAReference(Boolean replacedByAReference) {
		this.replacedByAReference = replacedByAReference;
	}

	public String getReplacesEan13() {
		return replacesEan13;
	}

	public void setReplacesEan13(String replacesEan13) {
		this.replacesEan13 = replacesEan13;
	}

	public String getReplacedByEan13() {
		return replacedByEan13;
	}

	public void setReplacedByEan13(String replacedByEan13) {
		this.replacedByEan13 = replacedByEan13;
	}

	public Boolean getOrderableByUnit() {
		return orderableByUnit;
	}

	public void setOrderableByUnit(Boolean orderableByUnit) {
		this.orderableByUnit = orderableByUnit;
	}

	public BarcodeType getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(BarcodeType barcodeType) {
		this.barcodeType = barcodeType;
	}

	public Boolean getMainReference() {
		return mainReference;
	}

	public void setMainReference(Boolean mainReference) {
		this.mainReference = mainReference;
	}

	public Boolean getSecondaryReference() {
		return secondaryReference;
	}

	public void setSecondaryReference(Boolean secondaryReference) {
		this.secondaryReference = secondaryReference;
	}

	public Integer getReferencesCount() {
		return referencesCount;
	}

	public void setReferencesCount(Integer referencesCount) {
		this.referencesCount = referencesCount;
	}
}
