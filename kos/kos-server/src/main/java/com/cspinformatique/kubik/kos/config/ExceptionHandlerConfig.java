package com.cspinformatique.kubik.kos.config;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cspinformatique.kubik.kos.exception.ExceptionProcessor;
import com.cspinformatique.kubik.kos.exception.ExceptionProcessors;
import com.cspinformatique.kubik.kos.rest.ErrorWrapper;

@ControllerAdvice
public class ExceptionHandlerConfig {

	@Resource
	private ExceptionProcessors exceptionProcessors;

	@ExceptionHandler
	public ResponseEntity<ErrorWrapper> handleInvalidFileFormatException(HttpServletRequest request, Exception ex)
			throws Exception {
		ExceptionProcessor<?> exceptionProcessor = exceptionProcessors.getExceptionProcessor(ex.getClass());

		if (exceptionProcessor == null) {
			throw ex;
		}

		return exceptionProcessor.processException(ex);
	}
}
