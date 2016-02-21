package com.cspinformatique.kubik.kos.model.account;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username") )
public class Account implements UserDetails {
	private static final long serialVersionUID = 4555169862756826354L;

	private long id;
	private String username;
	private String password;
	private List<Role> roles;
	private boolean shippingAddressPreferedForBilling;
	private Address billingAddress;
	private Address shippingAddress;

	public Account() {

	}

	public Account(long id, String username, String password, List<Role> roles,
			boolean shippingAddressPreferedForBilling, Address billingAddress, Address shippingAddress) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.shippingAddressPreferedForBilling = shippingAddressPreferedForBilling;
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(fetch = FetchType.EAGER)
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	@Transient
	public List<Role> getAuthorities() {
		return this.getRoles();
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}

	public boolean isShippingAddressPreferedForBilling() {
		return shippingAddressPreferedForBilling;
	}

	public void setShippingAddressPreferedForBilling(boolean shippingAddressPreferedForBilling) {
		this.shippingAddressPreferedForBilling = shippingAddressPreferedForBilling;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
}
