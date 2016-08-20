package com.cspinformatique.kubik.server.model.misc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {
	private int id;
	private String firstName;
	private String lastName;
	private String phone;
	private String streetLine1;
	private String streetLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	
	private long kosId;
	
	public Address(){
		
	}		

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public long getKosId() {
		return kosId;
	}

	public void setKosId(long kosId) {
		this.kosId = kosId;
	}
}
