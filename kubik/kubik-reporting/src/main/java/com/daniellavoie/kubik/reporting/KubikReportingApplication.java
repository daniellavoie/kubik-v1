package com.daniellavoie.kubik.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class KubikReportingApplication {
	public static void main(String[] args) {
		SpringApplication.run(KubikReportingApplication.class, args);
	}
}
