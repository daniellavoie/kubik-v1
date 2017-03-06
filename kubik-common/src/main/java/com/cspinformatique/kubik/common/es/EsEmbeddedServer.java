package com.cspinformatique.kubik.common.es;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty3Plugin;
import org.elasticsearch.transport.Netty4Plugin;

public class EsEmbeddedServer {
	private static class PluginConfigurableNode extends Node {
		public PluginConfigurableNode(Settings settings, Collection<Class<? extends Plugin>> classpathPlugins) {
			super(InternalSettingsPreparer.prepareEnvironment(settings, null), classpathPlugins);
		}
	}

	private final Node node;

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	public EsEmbeddedServer(String clusterName, String homePath, String dataPath, String httpRange,
			String transportRange) {
		Properties props = new Properties();
		props.setProperty("path.home", homePath);
		props.setProperty("path.data", dataPath);
		props.setProperty("http.port", httpRange);
		props.setProperty("transport.tcp.port", transportRange);
		props.setProperty("cluster.name", clusterName);
		props.setProperty("transport.type", "local");
		props.setProperty("http.type", "netty3");

		props.setProperty("script.inline", "true");

		props.setProperty("cluster.name", clusterName);
		props.setProperty("node.ingest", "true");

		Settings settings = Settings.builder().put(props).build();
		Collection<Class<? extends Plugin>> plugins = Arrays.asList(Netty3Plugin.class, Netty4Plugin.class);
		node = new PluginConfigurableNode(settings, plugins);
	}

	public Client getClient() {
		Client client = node.client();

		client.admin().cluster().prepareHealth().setWaitForYellowStatus().get();

		return client;
	}

	public void start() {
		try {
			node.start();
		} catch (Exception e) {
			throw new ElasticsearchException("Encountered exception during embedded node startup", e);
		}
	}

	public void stop() {
		try {
			node.close();
		} catch (IOException e) {
			throw new ElasticsearchException("Encountered exception during embdedded node shutdown", e);
		}
	}
}
