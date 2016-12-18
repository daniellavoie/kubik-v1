package com.cspinformatique.kubik.server.domain.system.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/environment")
public class EnvironmentController {
	private Environment environment;

	public EnvironmentController(Environment environment) {
		this.environment = environment;
	}

	@ResponseBody
	@PostMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getProperty(@RequestBody String property) {
		return environment.getProperty(property);
	}
}