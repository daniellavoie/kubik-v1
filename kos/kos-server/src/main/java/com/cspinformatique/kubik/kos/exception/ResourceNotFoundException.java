package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.common.rest.RestException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RestException {
	private static final long serialVersionUID = -1222215960859805617L;

	public ResourceNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
