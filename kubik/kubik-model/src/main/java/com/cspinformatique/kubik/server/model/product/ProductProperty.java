package com.cspinformatique.kubik.server.model.product;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(ProductPropertyPk.class)
public class ProductProperty {
	private Product product;
	private PropertyName name;
	private PropertyType type;
	private String value;

	public ProductProperty() {

	}

	public ProductProperty(Product product, PropertyName name, PropertyType type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	@Id
	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Id
	@Enumerated(EnumType.STRING)
	public PropertyName getName() {
		return name;
	}

	public void setName(PropertyName name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	public PropertyType getType() {
		return type;
	}

	public void setType(PropertyType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
