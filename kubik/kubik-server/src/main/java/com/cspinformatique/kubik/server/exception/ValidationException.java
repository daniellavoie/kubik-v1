package com.cspinformatique.kubik.server.exception;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = -4504818901791556404L;
	
	public ValidationException(String message){
		super(message);
	}

}
