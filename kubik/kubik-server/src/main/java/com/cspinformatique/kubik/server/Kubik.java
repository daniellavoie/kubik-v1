package com.cspinformatique.kubik.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.cspinformatique.kubik.server")
@EntityScan(basePackages = "com.cspinformatique.kubik.server")
@EnableJpaRepositories(basePackages = "com.cspinformatique.kubik.server")
@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class })
public class Kubik {
	public static void main(String args[]) {
		SpringApplication.run(Kubik.class);
	}
}