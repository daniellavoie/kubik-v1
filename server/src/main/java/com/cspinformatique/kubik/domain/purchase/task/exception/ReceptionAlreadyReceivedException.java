package com.cspinformatique.kubik.domain.purchase.task.exception;

public class ReceptionAlreadyReceivedException extends RuntimeException {
	private static final long serialVersionUID = 7246540127193683516L;
	
	private int receptionId;
	
	public ReceptionAlreadyReceivedException(int receptionId) {
		super("Reception " + receptionId + " has already been processed and closed.");
		
		this.receptionId = receptionId;
	}

	public int getReceptionId() {
		return receptionId;
	}
}
