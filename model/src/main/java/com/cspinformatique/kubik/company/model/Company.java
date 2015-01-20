package com.cspinformatique.kubik.company.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company {
	private String ean13;
	private String name;
	private String dilicomImageEncryptionKey;
	
	public Company(){
		
	}

	public Company(String ean13, String name, String dilicomImageEncryptionKey) {
		this.ean13 = ean13;
		this.name = name;
		this.dilicomImageEncryptionKey = dilicomImageEncryptionKey;
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
}
