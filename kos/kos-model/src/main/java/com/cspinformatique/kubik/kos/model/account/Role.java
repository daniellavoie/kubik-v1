package com.cspinformatique.kubik.kos.model.account;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1872845603045776256L;

	public enum Type {
		ADMIN, CUSTOMER
	}

	private int id;
	private String name;
	private Type type;

	public Role() {

	}

	public Role(int id, String name, Type type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Transient
	public String getAuthority() {
		return this.getName();
	}

	@Enumerated(EnumType.STRING)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
