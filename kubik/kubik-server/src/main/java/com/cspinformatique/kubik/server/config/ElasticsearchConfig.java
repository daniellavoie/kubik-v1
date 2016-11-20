package com.cspinformatique.kubik.server.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
	@Value("${elasticsearch.hostname}")
	private String hostname;

	@Value("${elasticsearch.port}")
	private int port;
	
	@Value("${elasticsearch.cluster.name}")
	private String clusterName;

	@Bean
	public Client client() throws UnknownHostException {
		return TransportClient.builder()
				.settings(Settings.settingsBuilder().put("cluster.name", clusterName).build()).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostname), port));
	}
}
