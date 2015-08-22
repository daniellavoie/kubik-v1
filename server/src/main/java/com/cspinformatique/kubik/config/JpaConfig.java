package com.cspinformatique.kubik.config;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.cspinformatique.kubik",
		"org.springframework.boot.error" }, excludeFilters = @Filter(type = FilterType.REGEX, pattern = "com.cspinformatique.kubik.domain.dilicom.repository.es.*") )
public class JpaConfig {

}
