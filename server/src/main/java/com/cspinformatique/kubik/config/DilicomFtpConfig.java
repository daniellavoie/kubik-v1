package com.cspinformatique.kubik.config;

import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
public class DilicomFtpConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(DilicomFtpConfig.class);
	
	@Value("${kubik.dilicom.ftp.url}")
	private String dilicomFtpUrl;

	@Value("${kubik.dilicom.ftp.username}")
	private String dilicomFtpUsername;

	@Value("${kubik.dilicom.ftp.password}")
	private String dilicomFtpPassword;
	
	public @Bean SessionFactory<FTPFile> dilicomFtpSessionFactory(){
		LOGGER.info("Configuring FTP session factory to host : " + dilicomFtpUrl);
		DefaultFtpSessionFactory dilicomFtpSessionFactory = new DefaultFtpSessionFactory();
		
		dilicomFtpSessionFactory.setHost(dilicomFtpUrl);
		dilicomFtpSessionFactory.setUsername(dilicomFtpUsername);
		dilicomFtpSessionFactory.setPassword(dilicomFtpPassword);
        
        return dilicomFtpSessionFactory;
	}
}
