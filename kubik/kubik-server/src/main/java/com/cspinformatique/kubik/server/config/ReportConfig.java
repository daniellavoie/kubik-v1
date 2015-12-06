package com.cspinformatique.kubik.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.XmlViewResolver;

@Configuration
public class ReportConfig {
	public @Bean XmlViewResolver xmlViewResolver(){
		XmlViewResolver xmlViewResolver = new XmlViewResolver();
		
		xmlViewResolver.setLocation(new ClassPathResource("reports/jasper-views.xml"));
		xmlViewResolver.setOrder(0);
		
		return xmlViewResolver;
	}
}
