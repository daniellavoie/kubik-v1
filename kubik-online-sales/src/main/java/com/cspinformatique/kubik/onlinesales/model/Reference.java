package com.cspinformatique.kubik.onlinesales.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "reference")
public class Reference {
	public enum Type {
		BOOK, MERCHANDISING
	}
	@Id
	private String id;
	
	@Field(type=FieldType.Integer)
	private int kubikId;
	
	@Field(type=FieldType.Long)
	private long kosId;
	
	@Field(type=FieldType.String)
	private String title;

	@Field(type=FieldType.String)
	private String author;
	
	@Field(type=FieldType.Integer)
	private int categoryId;
	
	@Field(type=FieldType.String)
	private String collection;

	@Field(type=FieldType.String)
	private String manufacturer;

	@Field(type=FieldType.String)
	private Type type;
	
	@Field(type=FieldType.String)
	private String isbn;
	
	@Field(type=FieldType.Double)
	private double price;
	
	@Field(type=FieldType.Date)
	private Date datePublished;
	
	public Reference(){
		
	}

	public Reference(String id, int kubikId, String title, String author, int categoryId, String collection,
			String manufacturer, Type type, String isbn, double price, Date datePublished) {
		this.id = id;
		this.kubikId = kubikId;
		this.title = title;
		this.author = author;
		this.categoryId = categoryId;
		this.collection = collection;
		this.manufacturer = manufacturer;
		this.type = type;
		this.isbn = isbn;
		this.price = price;
		this.datePublished = datePublished;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getKubikId() {
		return kubikId;
	}

	public void setKubikId(int kubikId) {
		this.kubikId = kubikId;
	}

	public long getKosId() {
		return kosId;
	}

	public void setKosId(long kosId) {
		this.kosId = kosId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}
}
