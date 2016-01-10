package com.cspinformatique.kubik;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import com.cspinformatique.kubik.server.config.ReportConfig;

@ComponentScan(basePackages = "com.cspinformatique.kubik.server", excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReportConfig.class) )
@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class })
public class KubikTest {

}
