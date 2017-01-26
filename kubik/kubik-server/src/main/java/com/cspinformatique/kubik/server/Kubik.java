package com.cspinformatique.kubik.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoConfiguration(exclude = { ElasticsearchRepositoriesAutoConfiguration.class })
public class Kubik {
	public static void main(String[] args) {
		SpringApplication.run(Kubik.class, args);
	}
}