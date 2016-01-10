
package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.common.rest.RestException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RestException {
	private static final long serialVersionUID = -3856095210114284286L;

	public BadRequestException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
