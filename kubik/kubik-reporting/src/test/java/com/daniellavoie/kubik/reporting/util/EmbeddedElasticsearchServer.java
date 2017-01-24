package com.daniellavoie.kubik.reporting.util;

import java.io.File;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.util.FileSystemUtils;

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
		FileSystemUtils.deleteRecursively(new File(dataDirectory));
	}
}
