package com.daniellavoie.kubik.product.vehicule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cspinformatique.kubik.common.rest.KubikTemplate;

@Configuration
public class KubikConfig {
	@Bean
	public KubikTemplate kubikTemplate(@Value("${kubik.url}") String url, @Value("${kubik.username}") String username,
			@Value("${kubik.password}") String password) {
		return new KubikTemplate(url, username, password);
	}
}
