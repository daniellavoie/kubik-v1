package com.cspinformatique.kubik.server.model.warehouse;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class StocktakingCategory {
	private long id;
	private Stocktaking stocktaking;

	private String name;
	private List<StocktakingProduct> products;
	private Date creationDate;

	public StocktakingCategory() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JsonBackReference
	public Stocktaking getStocktaking() {
		return stocktaking;
	}

	public void setStocktaking(Stocktaking stocktaking) {
		this.stocktaking = stocktaking;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "category")
	public List<StocktakingProduct> getProducts() {
		return products;
	}

	public void setProducts(List<StocktakingProduct> products) {
		this.products = products;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
