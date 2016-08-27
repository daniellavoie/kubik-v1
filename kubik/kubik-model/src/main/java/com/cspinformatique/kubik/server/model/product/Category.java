package com.cspinformatique.kubik.server.model.product;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Category {
	private int id;
	private String name;
	private boolean rootCategory;
	private Category parentCategory;
	private List<Category> childCategories;
	private boolean availableOnline;

	public Category() {

	}

	public Category(int id, String name, boolean rootCategory, List<Category> childCategories,
			boolean availableOnline) {
		this.id = id;
		this.name = name;
		this.rootCategory = rootCategory;
		this.childCategories = childCategories;
		this.availableOnline = availableOnline;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRootCategory() {
		return rootCategory;
	}

	public void setRootCategory(boolean rootCategory) {
		this.rootCategory = rootCategory;
	}

	@ManyToOne
	@JsonBackReference
	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parentCategory")
	public List<Category> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(List<Category> childCategories) {
		this.childCategories = childCategories;
	}

	public boolean isAvailableOnline() {
		return availableOnline;
	}

	public void setAvailableOnline(boolean availableOnline) {
		this.availableOnline = availableOnline;
	}
}
