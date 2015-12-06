package com.cspinformatique.kubik.kos.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

public class ServletConfig {
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
	        return new EmbeddedServletContainerCustomizer() {
	            @Override
	            public void customize(ConfigurableEmbeddedServletContainer container) {
	                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
	                container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
	                container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
	            }
	        };
	}
}
