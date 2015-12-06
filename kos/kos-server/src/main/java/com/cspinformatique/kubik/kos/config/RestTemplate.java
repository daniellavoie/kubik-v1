package com.cspinformatique.kubik.kos.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cspinformatique.kubik.kos.rest.KubikTemplate;

@Configuration
public class RestTemplate {
	@Resource
	private Environment environment;

	public @Bean KubikTemplate kubikTemplate() {
		return new KubikTemplate(environment);
	}
}
