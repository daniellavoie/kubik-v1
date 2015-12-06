package com.cspinformatique.kubik.server.model.purchase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShippingPackage {
	private Integer id;
	private String number;
	
	public ShippingPackage(){
		
	}
	
	public ShippingPackage(Integer id, String number){
		this.id = id;
		this.number = number;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
