package com.cspinformatique.kubik.kos.exception;

import org.springframework.http.ResponseEntity;

import com.cspinformatique.kubik.kos.rest.ErrorWrapper;

public interface ExceptionProcessor<T> {
	Class<T> supports();

	ResponseEntity<ErrorWrapper> processException(Exception ex);
}
