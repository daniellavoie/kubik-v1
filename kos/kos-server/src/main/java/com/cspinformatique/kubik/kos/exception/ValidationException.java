package com.cspinformatique.kubik.kos.exception;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 6790485477682898093L;
	
	public ValidationException(String message){  
		super(message);
	}
}
