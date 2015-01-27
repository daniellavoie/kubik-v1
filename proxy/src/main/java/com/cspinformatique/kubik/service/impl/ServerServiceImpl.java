package com.cspinformatique.kubik.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cspinformatique.kubik.proxy.model.Proxy;
import com.cspinformatique.kubik.service.ServerService;

@Service
public class ServerServiceImpl implements ServerService,
		ApplicationListener<EmbeddedServletContainerInitializedEvent> {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${kubik.proxy.server.url}")
	private String serverUrl;

	private Proxy proxy;

	@Override
	public void onApplicationEvent(
			EmbeddedServletContainerInitializedEvent event) {
		if (proxy == null) {
			try{
				InetAddress localHost;
			
				localHost = InetAddress.getLocalHost();

				this.proxy = new Proxy(null, localHost.getHostAddress(),
						localHost.getHostAddress(), event
								.getEmbeddedServletContainer().getPort());

				this.registerProxyToServer();
			} catch (UnknownHostException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void registerProxyToServer() {
		this.restTemplate
				.postForLocation(this.serverUrl + "/proxy", this.proxy);
	}
}
