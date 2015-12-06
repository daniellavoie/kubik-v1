package com.cspinformatique.kubik.server.domain.purchase.task.exception;

public class InvalidReceptionDetailException extends RuntimeException {
	private static final long serialVersionUID = 5181657371492217630L;

	public InvalidReceptionDetailException(String message){
		super(message);
	}
	
	public InvalidReceptionDetailException(String message, Throwable cause) {
		super(message, cause);
	}
}
