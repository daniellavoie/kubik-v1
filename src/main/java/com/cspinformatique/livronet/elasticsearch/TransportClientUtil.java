package com.cspinformatique.livronet.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.core.env.Environment;

public abstract class TransportClientUtil {
	public static TransportClient buildTransportClient(Environment env) {
		try (TransportClient transportClient = new TransportClient()) {

			for (String urlString : env.getRequiredProperty(
					"dilicom.sync.elasticsearch.urls").split(";")) {
				String[] url = urlString.split(":");

				if (url.length != 2) {
					throw new RuntimeException(
							"Property dilicom.sync.elasticsearch.urls is not properly defined.");
				}

				String hostname = url[0];
				int port = Integer.valueOf(url[1]);

				transportClient
						.addTransportAddress(new InetSocketTransportAddress(
								hostname, port));
			}

			return transportClient;
		}
	}
}
