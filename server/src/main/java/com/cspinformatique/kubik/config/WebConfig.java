package com.cspinformatique.kubik.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cspinformatique.kubik.domain.accounting.converter.EntryMessageConverter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		EntryMessageConverter entryMessageConverter = new EntryMessageConverter();
		
		converters.add(entryMessageConverter);
	}
}
