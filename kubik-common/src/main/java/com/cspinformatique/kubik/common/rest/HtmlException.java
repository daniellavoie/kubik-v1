package com.cspinformatique.kubik.common.rest;

public class HtmlException extends RuntimeException {
	private static final long serialVersionUID = 8558442137373122945L;

	private RestException restException;
	
	public HtmlException(RestException restException){
		this.restException = restException;
	}

	public RestException getRestException() {
		return restException;
	}
}
