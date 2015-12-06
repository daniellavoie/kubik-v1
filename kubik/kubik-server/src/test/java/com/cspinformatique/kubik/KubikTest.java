package com.cspinformatique.kubik;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

import com.cspinformatique.kubik.server.config.ReportConfig;

import org.springframework.context.annotation.FilterType;

@ComponentScan(basePackages = "com.cspinformatique.kubik.server", excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReportConfig.class) )
@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class, ElasticsearchAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class })
public class KubikTest {

}
