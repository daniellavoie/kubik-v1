package com.cspinformatique.kubik.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

@Configuration
@PropertySource(value = "braintree.properties")
public class BraintreeConfig {
	@Value("${braintree.environment}")
	private String environment;

	@Value("${braintree.merchant.id}")
	private String merchantId;

	@Value("${braintree.public.key}")
	private String publicKey;

	@Value("${braintree.private.key}")
	private String privateKey;

	public @Bean BraintreeGateway braintreeGateway() {
		return new BraintreeGateway(environment.equals("PRODUCTION") ? Environment.PRODUCTION : Environment.SANDBOX,
				merchantId, publicKey, privateKey);
	}
}
