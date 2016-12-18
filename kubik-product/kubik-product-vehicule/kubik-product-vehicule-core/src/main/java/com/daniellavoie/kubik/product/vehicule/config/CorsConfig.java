package com.daniellavoie.kubik.product.vehicule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CorsConfig {
	
	@Bean
    public WebMvcConfigurer corsConfigurer(@Value("${kubik.url}") String kubikUrl) {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/product").allowedOrigins(kubikUrl);
                registry.addMapping("/product/*").allowedOrigins(kubikUrl);
            }
        };
    }
}
