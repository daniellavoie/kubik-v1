package com.cspinformatique.kubik.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cspinformatique.kubik.server.rest.KubikReportingTemplate;

@Configuration
public class ReportingConfig {
	@Bean
	public KubikReportingTemplate kubikReportingTemplate(@Value("${kubik.reporting.url}") String url,
			@Value("${kubik.reporting.username}") String username,
			@Value("${kubik.reporting.password}") String password) {
		return new KubikReportingTemplate(url, username, password);
	}
}
