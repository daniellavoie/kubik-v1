package com.cspinformatique.kubik.kos.model.product;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Category {
	private int id;
	private String name;
	private boolean rootCategory;
	private Category parentCategory;
	private List<Category> childCategories;
	private int kubikId;

	public Category() {

	}

	public Category(int id, String name, boolean rootCategory, Category parentCategory,
			List<Category> childCategories, int kubikId) {
		this.id = id;
		this.name = name;
		this.rootCategory = rootCategory;
		this.parentCategory = parentCategory;
		this.childCategories = childCategories;
		this.kubikId = kubikId;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
	@JsonIgnore
	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@OneToMany(mappedBy = "parentCategory")
	public List<Category> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(List<Category> childCategories) {
		this.childCategories = childCategories;
	}

	public int getKubikId() {
		return kubikId;
	}

	public void setKubikId(int kubikId) {
		this.kubikId = kubikId;
	}
}
