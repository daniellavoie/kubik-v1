package com.cspinformatique.kubik.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.cspinformatique.kubik.elasticsearch.NodeClientUtil;
import com.cspinformatique.kubik.elasticsearch.TransportClientUtil;

@Configuration
@EnableElasticsearchRepositories(basePackages="com.cspinformatique.kubik.domain.reference")
@PropertySource("classpath:config/elasticsearch.properties")
public class ElasticsearchConfig {
	@Resource
	private Environment env;
	
	@Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
		if(env.getRequiredProperty("kubik.elasticsearch.node", Boolean.class)){
			return new ElasticsearchTemplate(NodeClientUtil.buildLocalNode());
		}else{
			return new ElasticsearchTemplate(TransportClientUtil.buildTransportClient(env));			
		}
    }
}
