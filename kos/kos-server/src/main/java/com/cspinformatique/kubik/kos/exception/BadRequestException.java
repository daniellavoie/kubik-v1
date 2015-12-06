
package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = -3856095210114284286L;

}
