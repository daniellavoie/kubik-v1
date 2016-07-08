package com.cspinformatique.kubik.server.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class EmbeddedElasticsearchServer {
	private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch-data";

	private final Node node;
	private final String dataDirectory;

	public EmbeddedElasticsearchServer() {
		this(DEFAULT_DATA_DIRECTORY);
	}

	public EmbeddedElasticsearchServer(String dataDirectory) {
		this.dataDirectory = dataDirectory;

		deleteDataDirectory();

		Settings.Builder elasticsearchSettings = Settings.builder().put("http.enabled", "t").put("path.home",
				dataDirectory);

		node = NodeBuilder.nodeBuilder().local(true).settings(elasticsearchSettings.build()).node();
	}

	public Client getClient() {
		return node.client();
	}

	public void shutdown() {
		node.close();
		deleteDataDirectory();
	}

	private void deleteDataDirectory() {
		try {
			FileUtils.deleteDirectory(new File(dataDirectory));
		} catch (IOException e) {
			throw new RuntimeException("Could not delete data directory of embedded elasticsearch server", e);
		}
	}
}
