package com.cspinformatique.kubik.print.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.http.HttpStatus;

@Entity
public class PrintJob {
	private Integer id;
	private Date startDate;
	private Date endDate;
	private HttpStatus status;
	private String proxyHostname;
	
	public PrintJob(){
		
	}

	public PrintJob(Integer id, Date startDate, Date endDate, HttpStatus status, String proxyHostname) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.proxyHostname = proxyHostname;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getProxyHostname() {
		return proxyHostname;
	}

	public void setProxyHostname(String proxyHostname) {
		this.proxyHostname = proxyHostname;
	}
}
