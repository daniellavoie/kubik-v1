package com.cspinformatique.kubik.common.rest;

public class ErrorWrapper {
	private String message;
	
	public ErrorWrapper(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
