package com.cspinformatique.kubik.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;

@Configuration
public class AkkaConfig {
	public @Bean ActorSystem actorSystem() {
		return ActorSystem.create("kubik");
	}
}
