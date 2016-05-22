package com.cspinformatique.kubik.server.model.company;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company {
	private String ean13;
	private String name;
	private String dilicomImageEncryptionKey;
	private String addressNumber;
	private String addressStreet;
	private String addressZipCode;
	private String addressCity;
	private String phone;
	private String website;
	private String email;
	private String siret;
	private String legalMention1;
	private String legalMention2;
	
	public Company(){
		
	}

	public Company(String ean13, String name, String dilicomImageEncryptionKey, String addressNumber,
			String addressStreet, String addressZipCode, String addressCity, String phone, String website, String email,
			String siret, String legalMention1, String legalMention2) {
		this.ean13 = ean13;
		this.name = name;
		this.dilicomImageEncryptionKey = dilicomImageEncryptionKey;
		this.addressNumber = addressNumber;
		this.addressStreet = addressStreet;
		this.addressZipCode = addressZipCode;
		this.addressCity = addressCity;
		this.phone = phone;
		this.website = website;
		this.email = email;
		this.siret = siret;
		this.legalMention1 = legalMention1;
		this.legalMention2 = legalMention2;
	}

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public String getDilicomImageEncryptionKey() {
		return dilicomImageEncryptionKey;
	}

	public void setDilicomImageEncryptionKey(String dilicomImageEncryptionKey) {
		this.dilicomImageEncryptionKey = dilicomImageEncryptionKey;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressZipCode() {
		return addressZipCode;
	}

	public void setAddressZipCode(String addressZipCode) {
		this.addressZipCode = addressZipCode;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public String getLegalMention1() {
		return legalMention1;
	}

	public void setLegalMention1(String legalMention1) {
		this.legalMention1 = legalMention1;
	}

	public String getLegalMention2() {
		return legalMention2;
	}

	public void setLegalMention2(String legalMention2) {
		this.legalMention2 = legalMention2;
	}
}
