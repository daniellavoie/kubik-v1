package com.cspinformatique.kubik.common.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchConfiguration {
	private String clusterName = "kubik-local";
	private String hostname = "localhost";
	private int port = 9300;

	@Value("${elasticsearch.embedded.data-path:target/es-data}")
	private String dataPath;

	@Value("${elasticsearch.embedded.home-path:target}")
	private String homePath;

	@Value("${elasticsearch.embedded.port-range:9200-9299}")
	private String portRange;

	@Value("${elasticsearch.embedded.transport-range:9200-9299}")
	private String transportRange;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	public String getPortRange() {
		return portRange;
	}

	public void setPortRange(String portRange) {
		this.portRange = portRange;
	}

	public String getTransportRange() {
		return transportRange;
	}

	public void setTransportRange(String transportRange) {
		this.transportRange = transportRange;
	}

	@Override
	public String toString() {
		return "ElasticsearchConfiguration [clusterName=" + clusterName + ", hostname=" + hostname + ", port=" + port
				+ ", dataPath=" + dataPath + ", homePath=" + homePath + ", portRange=" + portRange + ", transportRange="
				+ transportRange + "]";
	}
}
