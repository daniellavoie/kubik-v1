package com.cspinformatique.kubik.onlinesales.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.cspinformatique.kubik.onlinesales.social.SecurityContext;
import com.cspinformatique.kubik.onlinesales.social.SimpleConnectionSignUp;
import com.cspinformatique.kubik.onlinesales.social.SimpleSignInAdapter;
import com.cspinformatique.kubik.onlinesales.social.User;

@Configuration
public class SocialConfig {
	@Value("${spring.social.facebook.appId}")
	private String facebookAppId;

	@Value("${spring.social.facebook.appSecret}")
	private String facebookAppSecret;

	@Value("${spring.social.twitter.appId}")
	private String twitterAppId;

	@Value("${spring.social.twitter.appSecret}")
	private String twitterSecretId;

	@Resource
	private DataSource dataSource;

	public @Bean ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

		registry.addConnectionFactory(new FacebookConnectionFactory(facebookAppId, facebookAppSecret));

		return registry;
	}

	public @Bean UsersConnectionRepository usersConnectionRepository() {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator(), Encryptors.noOpText());

		repository.setConnectionSignUp(new SimpleConnectionSignUp());

		return repository;
	}

	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public @Bean ConnectionRepository connectionRepository() {
		User user = SecurityContext.getCurrentUser();

		return usersConnectionRepository().createConnectionRepository(user.getId());
	}
	
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)   
	public @Bean Facebook facebook() {
	    return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
	}
	
	@Bean
	public ProviderSignInController providerSignInController() {
	    return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(),
	        new SimpleSignInAdapter());
	}
	
	private DatabasePopulator databasePopulator() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql", JdbcUsersConnectionRepository.class));
		return populator;
	}
}
