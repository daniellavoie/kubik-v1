package com.cspinformatique.kubik.common.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface RestQuery<T> {
	public ResponseEntity<T> executeQuery(RestTemplate restTemplate);
}
