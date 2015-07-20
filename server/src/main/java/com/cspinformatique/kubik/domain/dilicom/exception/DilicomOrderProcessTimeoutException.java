package com.cspinformatique.kubik.domain.dilicom.exception;

public class DilicomOrderProcessTimeoutException extends RuntimeException {
	private static final long serialVersionUID = 7145148983833223985L;
	
	public DilicomOrderProcessTimeoutException(String message){
		super(message);
	}

}
