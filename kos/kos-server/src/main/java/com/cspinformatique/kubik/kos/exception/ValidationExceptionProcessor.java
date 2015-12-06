package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.kos.rest.ErrorWrapper;

@Component
public class ValidationExceptionProcessor implements ExceptionProcessor<ValidationException> {

	@Override
	public Class<ValidationException> supports() {
		return ValidationException.class;
	}

	@Override
	public ResponseEntity<ErrorWrapper> processException(Exception ex) {
		ValidationException validationEx = (ValidationException) ex;

		return new ResponseEntity<ErrorWrapper>(new ErrorWrapper(validationEx.getMessage()), HttpStatus.BAD_REQUEST);
	}

}
