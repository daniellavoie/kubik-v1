package com.cspinformatique.kubik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class,
		ElasticsearchAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class })
public class Kubik {
	public static void main(String args[]) {
		SpringApplication.run(Kubik.class);
	}
}