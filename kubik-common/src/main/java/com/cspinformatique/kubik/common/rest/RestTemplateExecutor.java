package com.cspinformatique.kubik.common.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class RestTemplateExecutor {
	private RestTemplate restTemplate;

	public RestTemplateExecutor() {
		restTemplate = new RestTemplate();
	}

	public <T> T execute(RestQuery<T> restQuery) {
		try {
			ResponseEntity<T> responseEntity = restQuery.executeQuery(restTemplate);

			HttpStatus status = responseEntity.getStatusCode();

			if (!status.equals(HttpStatus.OK) && !status.equals(HttpStatus.NO_CONTENT)) {
				String reason = responseEntity.getHeaders().getFirst(RestHeaders.REASON.getHeaderName());
				String codeStr = responseEntity.getHeaders().getFirst(RestHeaders.ERROR_CODE.getHeaderName());

				throw new RestException(reason, responseEntity.getStatusCode(),
						codeStr != null ? Integer.parseInt(codeStr) : null);
			}

			return responseEntity.getBody();
		} catch (HttpServerErrorException httpServerErrorEx) {
			HttpHeaders headers = httpServerErrorEx.getResponseHeaders();
			String reason = headers.getFirst(RestHeaders.REASON.getHeaderName());
			String codeStr = headers.getFirst(RestHeaders.ERROR_CODE.getHeaderName());

			throw new RestException(reason, httpServerErrorEx.getStatusCode(),
					codeStr != null ? Integer.parseInt(codeStr) : null);
		}
	}
}
