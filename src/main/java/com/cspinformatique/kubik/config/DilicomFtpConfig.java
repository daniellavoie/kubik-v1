package com.cspinformatique.kubik.config;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
public class DilicomFtpConfig {
	@Value("${kubik.reference.dilicom.ftp.url}")
	private String dilicomFtpUrl;

	@Value("${kubik.reference.dilicom.ftp.username}")
	private String dilicomFtpUsername;

	@Value("${kubik.reference.dilicom.ftp.password}")
	private String dilocomFtpPassword;
	
	public @Bean SessionFactory<FTPFile> dilicomFtpSessionFactory(){
		DefaultFtpSessionFactory dilicomFtpSessionFactory = new DefaultFtpSessionFactory();
		
		dilicomFtpSessionFactory.setHost(dilicomFtpUrl);
		dilicomFtpSessionFactory.setClientMode(0);
		dilicomFtpSessionFactory.setFileType(0);
		dilicomFtpSessionFactory.setPort(21);
		dilicomFtpSessionFactory.setUsername(dilicomFtpUsername);
		dilicomFtpSessionFactory.setPassword(dilocomFtpPassword);
        
        return dilicomFtpSessionFactory;
	}
}
