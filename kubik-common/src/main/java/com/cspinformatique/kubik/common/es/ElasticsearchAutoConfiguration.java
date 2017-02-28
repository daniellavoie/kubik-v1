package com.cspinformatique.kubik.common.es;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticsearchAutoConfiguration {
	@Autowired
	private Environment environment;

	private Client client;

	@Bean
	@SuppressWarnings("resource")
	@ConditionalOnProperty(name = "elasticsearch.embedded", havingValue = "false", matchIfMissing = false)
	public Client transportClient() throws IllegalStateException, UnknownHostException {
		client = new PreBuiltTransportClient(Settings.builder()
				.put("cluster.name", environment.getRequiredProperty("elasticsearch.cluster.name")).build())
						.addTransportAddress(new InetSocketTransportAddress(
								InetAddress.getByName(environment.getRequiredProperty("elasticsearch.hostname")),
								environment.getRequiredProperty("elasticsearch.port", Integer.class)));

		return client;
	}

	@PreDestroy
	public void destroy() {
		client.close();
	}
}
