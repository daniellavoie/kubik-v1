package com.cspinformatique.kubik.config;

import java.util.List;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cspinformatique.kubik.domain.accounting.converter.AccountsMessageConverter;
import com.cspinformatique.kubik.domain.accounting.converter.EntryMessageConverter;
import com.cspinformatique.kubik.domain.warehouse.converter.InventoryExtractConverter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new AccountsMessageConverter());
		converters.add(new EntryMessageConverter());
		converters.add(new InventoryExtractConverter());
	}

	@Bean
	public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer servletContainer) {
				MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
				mappings.add("html", "text/html;charset=utf-8");
				servletContainer.setMimeMappings(mappings);

				((TomcatEmbeddedServletContainerFactory) servletContainer)
						.addConnectorCustomizers(new TomcatConnectorCustomizer() {
					@Override
					public void customize(Connector connector) {
						AbstractHttp11Protocol<?> httpProtocol = (AbstractHttp11Protocol<?>) connector
								.getProtocolHandler();
						httpProtocol.setCompression("on");
						httpProtocol.setCompressionMinSize(256);
						String mimeTypes = httpProtocol.getCompressableMimeTypes();
						String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
						httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
					}
				});
			}
		};
	}
}
