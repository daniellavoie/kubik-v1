package com.cspinformatique.kubik.server.model.product;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {
	private int id;
	private String name;
	private boolean rootCategory;
	private List<Category> childCategories;

	public Category() {

	}

	public Category(int id, String name, boolean rootCategory, List<Category> childCategories) {
		this.id = id;
		this.name = name;
		this.rootCategory = rootCategory;
		this.childCategories = childCategories;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(unique=true)
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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Category> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(List<Category> childCategories) {
		this.childCategories = childCategories;
	}
}
