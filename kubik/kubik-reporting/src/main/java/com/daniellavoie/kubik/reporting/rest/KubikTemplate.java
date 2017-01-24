package com.daniellavoie.kubik.reporting.rest;

import java.util.Base64;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class KubikTemplate {
	private RestTemplate restTemplate;
	private String url;
	private HttpHeaders authorizationHeader;

	public KubikTemplate(String url, String username, String password) {
		this.restTemplate = new RestTemplate();
		this.url = url;

		authorizationHeader = KubikTemplate.createBasicAuthHeader(username, password);
	}

	public <T> ResponseEntity<T> exchange(String ressource, HttpMethod method, Class<T> responseType) {
		return restTemplate.exchange(url + ressource, method, new HttpEntity<>(authorizationHeader), responseType);
	}

	public <T> ResponseEntity<T> exchange(String ressource, HttpMethod method,
			ParameterizedTypeReference<T> responseType) {
		return restTemplate.exchange(url + ressource, method, new HttpEntity<>(authorizationHeader), responseType);
	}

	public <T, S> ResponseEntity<T> exchange(String ressource, HttpMethod method, S body, Class<T> responseType) {
		return restTemplate.exchange(url + ressource, method, new HttpEntity<>(body, authorizationHeader),
				responseType);
	}

	private static HttpHeaders createBasicAuthHeader(final String username, final String password) {
		return new HttpHeaders() {
			private static final long serialVersionUID = 1766341693637204893L;

			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
				String authHeader = "Basic " + new String(encodedAuth);
				this.set("Authorization", authHeader);
			}
		};
	}
}
