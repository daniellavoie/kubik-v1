package com.cspinformatique.kubik.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
	public @Bean RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
