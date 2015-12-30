package com.cspinformatique.kubik.kos.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class KubikNotification {
	public enum Action {
		UPDATE, DELETE
	}

	public enum Status {
		TO_PROCESS, SKIPPED, PROCESSED, ERROR
	}

	public enum Type {
		ACCOUNT, CUSTOMER_ORDER
	}

	private long id;
	private long kosId;
	private Status status;
	private Action action;
	private Type type;
	private Date creationDate;
	private Date processedDate;
	private Date errorDate;
	private String error;

	public KubikNotification() {
		
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKosId() {
		return kosId;
	}

	public void setKosId(long kosId) {
		this.kosId = kosId;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Enumerated(EnumType.STRING)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public Date getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}

	@Lob
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
