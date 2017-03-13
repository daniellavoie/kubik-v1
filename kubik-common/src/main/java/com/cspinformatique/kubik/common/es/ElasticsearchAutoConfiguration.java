package com.cspinformatique.kubik.common.es;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ElasticsearchConfiguration.class)
public class ElasticsearchAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchAutoConfiguration.class);

	private Client client;
	private EsEmbeddedServer server;

	@Bean
	@SuppressWarnings("resource")
	@ConditionalOnProperty(name = "elasticsearch.embedded.enabled", havingValue = "false", matchIfMissing = true)
	public Client transportClient(ElasticsearchConfiguration elasticsearchConfiguration)
			throws IllegalStateException, UnknownHostException {

		LOGGER.info("Initializing transport client with " + elasticsearchConfiguration);
		
		client = new PreBuiltTransportClient(
				Settings.builder().put("cluster.name", elasticsearchConfiguration.getClusterName()).build())
						.addTransportAddress(new InetSocketTransportAddress(
								InetAddress.getByName(elasticsearchConfiguration.getHostname()),
								elasticsearchConfiguration.getPort()));

		return client;
	}

	@Bean
	@ConditionalOnProperty(name = "elasticsearch.embedded.enabled")
	public Client embeddedClient(ElasticsearchConfiguration elasticsearchConfiguration) {
		server = new EsEmbeddedServer(elasticsearchConfiguration.getClusterName(),
				elasticsearchConfiguration.getHomePath(), elasticsearchConfiguration.getDataPath(),
				elasticsearchConfiguration.getPortRange(), elasticsearchConfiguration.getTransportRange());

		server.start();

		return server.getClient();
	}

	@PreDestroy
	public void destroy() {
		client.close();

		if (server != null)
			server.stop();
	}
}
