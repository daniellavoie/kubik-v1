package com.cspinformatique.kubik.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.cspinformatique.kubik", excludeFilters = @Filter(type = FilterType.REGEX, pattern = "com.cspinformatique.kubik.reference.*"))
public class JpaConfig {

}
