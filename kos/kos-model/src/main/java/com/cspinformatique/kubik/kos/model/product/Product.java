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
	private String title;
	private String author;
	private String collection;
	private String manufacturer;
	private String isbn;
	private double price;
	private Date datePublished;
	private Category category;
	private List<ProductImage> images;
	
	private Integer thickness;
	private Integer width;
	private Integer height;
	private Integer weight;

	public Product() {

	}

	public Product(int id, int kubikId, String title, String author, int categoryId, String collection,
			String manufacturer, String isbn, double price, Date datePublished, Category category,
			List<ProductImage> images, Integer thickness, Integer width, Integer height, Integer weight) {
		this.id = id;
		this.kubikId = kubikId;
		this.title = title;
		this.author = author;
		this.collection = collection;
		this.manufacturer = manufacturer;
		this.isbn = isbn;
		this.price = price;
		this.datePublished = datePublished;
		this.category = category;
		this.images = images;
		this.thickness = thickness;
		this.width = width;
		this.height = height;
		this.weight = weight;
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
}
