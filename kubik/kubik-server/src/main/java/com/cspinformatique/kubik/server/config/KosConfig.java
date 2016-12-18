package com.cspinformatique.kubik.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cspinformatique.kubik.common.rest.KubikTemplate;

@Configuration
public class KosConfig {
	@Bean
	public KubikTemplate kosTemplate(@Value("${kos.notification.url}") String url,
			@Value("${kos.notification.username}") String username,
			@Value("${kos.notification.password}") String password) {
		return new KubikTemplate(url, username, password);
	}
}
