package com.cspinformatique.kubik.domain.kos.rest;

import java.util.Base64;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class KosTemplate {
	private RestTemplate restTemplate;
	private String kosUrl;
	private HttpHeaders authorizationHeader;

	public KosTemplate(Environment environment) {
		this.restTemplate = new RestTemplate();

		kosUrl = environment.getRequiredProperty("kos.notification.url");
		authorizationHeader = KosTemplate.createBasicAuthHeader(
				environment.getRequiredProperty("kos.notification.username"),
				environment.getRequiredProperty("kos.notification.password"));
	}

	public <T> ResponseEntity<T> exchange(String ressource, HttpMethod method, Class<T> responseType) {
		return restTemplate.exchange(kosUrl + ressource, method, new HttpEntity<>(authorizationHeader), responseType);
	}

	public <T, S> ResponseEntity<T> exchange(String ressource, HttpMethod method, S body, Class<T> responseType) {
		return restTemplate.exchange(kosUrl + ressource, method, new HttpEntity<>(body, authorizationHeader),
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
