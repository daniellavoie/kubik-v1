package com.cspinformatique.kubik.common.es;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticsearchAutoConfiguration {
	@Autowired
	private Environment environment;

	@Bean
	@ConditionalOnProperty(name = "elasticsearch.embedded", matchIfMissing = true)
	public Client embeddedClusterClient() {
		Builder settingsBuilder = Settings.settingsBuilder();

		settingsBuilder.put("node.name", "embdded");
		settingsBuilder.put("path.data", environment.getRequiredProperty("elasticsearch.path.data"));
		settingsBuilder.put("path.home", environment.getRequiredProperty("elasticsearch.path.home"));
		settingsBuilder.put("http.enabled", false);

		Settings settings = settingsBuilder.build();

		Node node = NodeBuilder.nodeBuilder().settings(settings).clusterName("kubik").data(true).local(true).node();

		return node.client();
	}

	@Bean
	@ConditionalOnProperty(name = "elasticsearch.embedded", havingValue="false", matchIfMissing = false)
	public Client transportClient() throws IllegalStateException, UnknownHostException {
		return TransportClient.builder()
				.settings(Settings.settingsBuilder()
						.put("cluster.name", environment.getRequiredProperty("elasticsearch.cluster.name")).build())
				.build()
				.addTransportAddress(new InetSocketTransportAddress(
						InetAddress.getByName(environment.getRequiredProperty("elasticsearch.hostname")),
						environment.getRequiredProperty("elasticsearch.port", Integer.class)));
	}
}
