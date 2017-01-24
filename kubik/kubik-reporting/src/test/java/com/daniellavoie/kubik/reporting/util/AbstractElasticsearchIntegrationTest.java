package com.daniellavoie.kubik.reporting.util;

import org.elasticsearch.client.Client;
import org.junit.Before;

public abstract class AbstractElasticsearchIntegrationTest {
	private EmbeddedElasticsearchServer embeddedElasticsearchServer;

    @Before
    public void startEmbeddedElasticsearchServer() {
        embeddedElasticsearchServer = new EmbeddedElasticsearchServer();
    }
    
    public void shutdownEmbeddedElasticsearchServer() {
        embeddedElasticsearchServer.shutdown();
    }
    
    protected Client getClient() {
        return embeddedElasticsearchServer.getClient();
    }
}
