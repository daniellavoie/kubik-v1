package com.cspinformatique.kubik.kos.model.product;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Product {
	public enum Type {
		BOOK, MERCHANDISING
	}

	private int id;
	private int kubikId;
	private String name;
	private String brand;
	private String collection;
	private String manufacturer;
	private String ean13;
	private String isbn;
	private double price;
	private Date datePublished;
	private Category category;
	private List<ProductImage> images;

	private Integer thickness;
	private Integer width;
	private Integer height;
	private Integer weight;
	private boolean available;

	public Product() {

	}

	public Product(int id, int kubikId, String title, String brand, int categoryId, String collection,
			String manufacturer, String ean13, String isbn, double price, Date datePublished, Category category,
			List<ProductImage> images, Integer thickness, Integer width, Integer height,
			Integer weight, boolean available) {
		this.id = id;
		this.kubikId = kubikId;
		this.name = title;
		this.brand = brand;
		this.collection = collection;
		this.manufacturer = manufacturer;
		this.ean13 = ean13;
		this.isbn = isbn;
		this.price = price;
		this.datePublished = datePublished;
		this.category = category;
		this.images = images;
		this.thickness = thickness;
		this.width = width;
		this.height = height;
		this.weight = weight;
		this.available = available;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKubikId() {
		return kubikId;
	}

	public void setKubikId(int kubikId) {
		this.kubikId = kubikId;
	}

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String author) {
		this.brand = author;
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

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
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

	@ManyToOne
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
