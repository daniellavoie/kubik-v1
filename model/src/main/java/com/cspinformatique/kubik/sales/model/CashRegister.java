package com.cspinformatique.kubik.sales.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CashRegister {
	private String hostname;
	private String ip;
	
	public CashRegister(){
	
	}

	public CashRegister(String hostname, String ip) {
		this.hostname = hostname;
		this.ip = ip;
	}

	@Id
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
