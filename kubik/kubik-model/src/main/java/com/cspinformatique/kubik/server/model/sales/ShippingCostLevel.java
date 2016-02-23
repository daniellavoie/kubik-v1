package com.cspinformatique.kubik.server.model.sales;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ShippingCostLevel {
	private int id;
	private Integer weight;
	private double cost;

	public ShippingCostLevel() {

	}

	public ShippingCostLevel(int id, Integer weight, double cost) {
		this.id = id;
		this.weight = weight;
		this.cost = cost;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}
