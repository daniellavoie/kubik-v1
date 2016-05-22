package com.cspinformatique.kubik.server.model.system;

import com.cspinformatique.kubik.server.model.company.Company;

public class Initialization {
	private Company company;
	private String username;
	private String password;

	public Initialization() {

	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
