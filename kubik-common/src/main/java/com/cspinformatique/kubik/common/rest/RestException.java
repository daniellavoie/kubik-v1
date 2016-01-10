package com.cspinformatique.kubik.common.rest;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
	private static final long serialVersionUID = -5661369090163956880L;

	private HttpStatus httpStatus;
	private Integer errorCode;

	public RestException(String message, HttpStatus httpStatus) {
		this(message, httpStatus, null);
	}

	public RestException(String message, HttpStatus httpStatus, Integer errorCode) {
		super(message);

		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public Integer getErrorCode() {
		return errorCode;
	}
}
