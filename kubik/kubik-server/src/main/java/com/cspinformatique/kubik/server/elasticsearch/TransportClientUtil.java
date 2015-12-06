package com.cspinformatique.kubik.server.elasticsearch;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.core.env.Environment;

public abstract class TransportClientUtil {
	@SuppressWarnings("resource")
	public static Client buildTransportClient(Environment env) {
		TransportClient client = new TransportClient(ImmutableSettings
				.settingsBuilder().put("client.transport.ignore_cluster_name", true).build());
		
		for (String urlString : env.getRequiredProperty(
				"kubik.elasticsearch.urls").split(",")) {
			String[] url = urlString.split(":");

			if (url.length != 2) {
				throw new RuntimeException(
						"Property kubik.elasticsearch.urls is not properly defined.");
			}

			String hostname = url[0];
			int port = Integer.valueOf(url[1]);

			client.addTransportAddress(new InetSocketTransportAddress(
					hostname, port));
		}
		
		try {
			client.admin().cluster().health(new ClusterHealthRequest()).get().getStatus();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		return (Client) client;
	}
}
