package com.cspinformatique.kubik.config;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@PropertySource("classpath:config/thymeleaf.properties")
public class ThymeleafConfig {
	@Resource
	Environment env;

	public @Bean TemplateResolver defaultTemplateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();

		templateResolver.setPrefix("/html/");
		templateResolver.setSuffix(env.getRequiredProperty("thymeleaf.suffix"));
		templateResolver.setTemplateMode(env
				.getRequiredProperty("thymeleaf.templateMode"));
		templateResolver.setCacheable(false);
		templateResolver.setCharacterEncoding("UTF-8");

		return templateResolver;
	}

	public @Bean TemplateResolver classLoaderTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

		templateResolver.setPrefix("html/");
		templateResolver.setSuffix(env.getRequiredProperty("thymeleaf.suffix"));
		templateResolver.setTemplateMode(env
				.getRequiredProperty("thymeleaf.templateMode"));
		templateResolver.setCacheable(false);
		templateResolver.setCharacterEncoding("UTF-8");

		return templateResolver;
	}

	public @Bean SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();

		templateEngine.setTemplateResolvers(new HashSet<ITemplateResolver>(
				Arrays.asList(new ITemplateResolver[] {
						defaultTemplateResolver()
						,classLoaderTemplateResolver()
						})));

		return templateEngine;
	}

	public @Bean ViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();

		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");

		return thymeleafViewResolver;
	}
}
