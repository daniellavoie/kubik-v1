package com.cspinformatique.kubik.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfiguration {
	@Autowired(required = false)
	private JavaMailSender sender;

	public JavaMailSender getSender() {
		return sender;
	}
}
