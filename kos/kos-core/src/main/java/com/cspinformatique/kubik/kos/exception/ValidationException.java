package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.HttpStatus;

import com.cspinformatique.kubik.common.rest.RestException;

public class ValidationException extends RestException {
	private static final long serialVersionUID = 6790485477682898093L;
	
	public ValidationException(String message){  
		super(message, HttpStatus.BAD_REQUEST);
	}
}
