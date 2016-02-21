package com.cspinformatique.kubik.server.model.sales;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cspinformatique.kubik.server.model.misc.Address;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email") )
public class Customer {
	private Integer id;
	private String lastName;
	private String firstName;
	private String companyName;
	private String fixedPhone;
	private String mobilePhone;
	private String email;
	private String ean13;
	private String internalNote;
	private Address address;
	private Date creationDate;
	private String tvaNumber;

	public Customer() {

	}

	public Customer(Integer id) {
		this.id = id;
	}

	public Customer(Integer id, String lastName, String firstName, String companyName, String fixedPhone,
			String mobilePhone, String email, String ean13, String internalNote, Address address, Date creationDate, String tvaNumber) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.companyName = companyName;
		this.fixedPhone = fixedPhone;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.ean13 = ean13;
		this.internalNote = internalNote;
		this.address = address;
		this.creationDate = creationDate;
		this.tvaNumber = tvaNumber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFixedPhone() {
		return fixedPhone;
	}

	public void setFixedPhone(String fixedPhone) {
		this.fixedPhone = fixedPhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	@Column(columnDefinition = "TEXT")
	public String getInternalNote() {
		return internalNote;
	}

	public void setInternalNote(String internalNote) {
		this.internalNote = internalNote;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTvaNumber() {
		return tvaNumber;
	}

	public void setTvaNumber(String tvaNumber) {
		this.tvaNumber = tvaNumber;
	}
}
