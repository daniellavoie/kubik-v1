package com.cspinformatique.kubik.server.config;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cspinformatique.kubik.common.rest.RestException;
import com.cspinformatique.kubik.common.rest.RestHeaders;
import com.cspinformatique.kubik.server.error.model.Error;
import com.cspinformatique.kubik.server.error.service.ErrorService;
import com.cspinformatique.kubik.server.exception.ValidationException;

@ControllerAdvice
public class ExceptionHandlerConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

	@Autowired
	ErrorService errorService;

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Error> handleInvalidFileFormatException(HttpServletRequest request, RuntimeException ex) {
		Error error = new Error(0, new Date(), request.getRemoteAddr(), request.getRemoteHost(),
				request.getHeader("User-Agent"), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex));

		if (ex instanceof ValidationException) {
			return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
		}

		LOGGER.error("Error while processing request.", ex);

		return new ResponseEntity<Error>(errorService.save(error), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RestException.class)
	public void handleInvalidFileFormatException(HttpServletResponse response, RestException ex) throws Exception {
		if (LOGGER.isDebugEnabled())
			LOGGER.error(ex.getMessage(), ex);
		else
			LOGGER.error(ex.getMessage());

		response.setStatus(ex.getHttpStatus().value());
		response.addHeader(RestHeaders.REASON.getHeaderName(), ex.getMessage());

		if (ex.getErrorCode() != null)
			response.addHeader(RestHeaders.ERROR_CODE.getHeaderName(), ex.getErrorCode().toString());
	}
}
