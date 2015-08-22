package com.cspinformatique.kubik.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.cspinformatique.broadleaf.client.BroadleafTemplate;

@Configuration
public class BroadleafConfig {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${broadleaf.url}")
	private String url;

	@Resource
	private Environment env;
	
	public @Bean BroadleafTemplate broadleafTemplate(){
		return new BroadleafTemplate(url, env.getProperty("broadleaf.username"),
				env.getProperty("broadleaf.password"), restTemplate);
	}
}
