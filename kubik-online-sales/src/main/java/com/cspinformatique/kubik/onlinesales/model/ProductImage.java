package com.cspinformatique.kubik.onlinesales.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class ProductImage {
	private long id;
	private Product product;
	private ProductImageSize size;

	public ProductImage() {

	}

	public ProductImage(long id, Product product, ProductImageSize size) {
		this.id = id;
		this.product = product;
		this.size = size;
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JsonInclude
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Enumerated(EnumType.STRING)
	public ProductImageSize getSize() {
		return size;
	}

	public void setSize(ProductImageSize size) {
		this.size = size;
	}
}
