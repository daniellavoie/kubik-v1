package com.cspinformatique.kubik.kos.config;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cspinformatique.kubik.common.rest.ErrorWrapper;
import com.cspinformatique.kubik.common.rest.HtmlException;
import com.cspinformatique.kubik.common.rest.RestException;
import com.cspinformatique.kubik.common.rest.RestHeaders;
import com.cspinformatique.kubik.kos.exception.ValidationException;

@ControllerAdvice
public class ExceptionHandlerConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

	@ExceptionHandler(RestException.class)
	public void processRestException(HttpServletResponse response, RestException ex) throws Exception {
		if (LOGGER.isDebugEnabled())
			LOGGER.error(ex.getMessage(), ex);
		else
			LOGGER.error(ex.getMessage());

		response.setStatus(ex.getHttpStatus().value());
		response.addHeader(RestHeaders.REASON.getHeaderName(), ex.getMessage());

		if (ex.getErrorCode() != null)
			response.addHeader(RestHeaders.ERROR_CODE.getHeaderName(), ex.getErrorCode().toString());
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorWrapper> processException(Exception ex) {
		return new ResponseEntity<ErrorWrapper>(new ErrorWrapper(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HtmlException.class)
	public String processHtmlException(HtmlException ex) {
		return "redirect:/" + ex.getRestException().getHttpStatus().value();
	}
}
