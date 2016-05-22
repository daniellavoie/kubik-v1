package com.cspinformatique.kubik.server.config;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.cspinformatique.kubik.server.elasticsearch.NodeClientUtil;
import com.cspinformatique.kubik.server.elasticsearch.TransportClientUtil;

@Configuration
@ConditionalOnProperty(name = "kubik.dilicom.enabled")
@PropertySource("classpath:config/elasticsearch.properties")
@EnableElasticsearchRepositories(basePackages="com.cspinformatique.kubik.server.domain.dilicom.repository.es")
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
