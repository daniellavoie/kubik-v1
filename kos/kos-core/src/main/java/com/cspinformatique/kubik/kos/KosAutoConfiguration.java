package com.cspinformatique.kubik.kos;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableJpaRepositories()
@EntityScan(basePackages = "com.cspinformatique.kubik.kos.model")
public class KosAutoConfiguration {

}
