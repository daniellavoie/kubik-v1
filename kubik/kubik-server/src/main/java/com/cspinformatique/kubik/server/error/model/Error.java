package com.cspinformatique.kubik.server.error.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Error {
	private long id;
	private Date timestamp;
	private String ip;
	private String hostname;
	private String agent;
	private String message;
	private String exception;

	public Error() {

	}

	public Error(long id, Date timestamp, String ip, String hostname, String agent, String message, String exception) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.ip = ip;
		this.hostname = hostname;
		this.agent = agent;
		this.message = message;
		this.exception = exception;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@Lob
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Lob 
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
}
