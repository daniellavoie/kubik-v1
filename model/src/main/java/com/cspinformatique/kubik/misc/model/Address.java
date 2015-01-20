package com.cspinformatique.kubik.misc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {
	private int id;
	private String streetLine1;
	private String streetLine2;
	private String city;
	private String state;
	private String zipCode;
	
	public Address(){
		
	}

	public Address(int id, String streetLine1, String streetLine2, String city,
			String state, String zipCode) {
		this.id = id;
		this.streetLine1 = streetLine1;
		this.streetLine2 = streetLine2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreetLine1() {
		return streetLine1;
	}

	public void setStreetLine1(String streetLine1) {
		this.streetLine1 = streetLine1;
	}

	public String getStreetLine2() {
		return streetLine2;
	}

	public void setStreetLine2(String streetLine2) {
		this.streetLine2 = streetLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
