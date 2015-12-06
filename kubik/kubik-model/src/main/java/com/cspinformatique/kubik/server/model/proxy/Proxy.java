package com.cspinformatique.kubik.server.model.proxy;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Proxy {
	private Integer id;
	private String hostname;
	private String ip;
	private int port;
	
	public Proxy(){
		
	}

	public Proxy(Integer id, String hostname, String ip, int port) {
		this.id = id;
		this.hostname = hostname;
		this.ip = ip;
		this.port = port;
	}

	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
