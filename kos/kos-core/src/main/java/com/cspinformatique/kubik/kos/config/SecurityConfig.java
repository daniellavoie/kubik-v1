package com.cspinformatique.kubik.kos.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cspinformatique.kubik.kos.domain.account.service.AccountService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	private AccountService accountService;

	@Resource
	private Environment env;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(accountService).passwordEncoder(passwordEncoder()).and().inMemoryAuthentication()
				.withUser(env.getRequiredProperty("kos.username")).password(env.getRequiredProperty("kos.password"))
				.roles("SYSTEM");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/compte/connexion").failureUrl("/compte/connexion?error")
				.loginProcessingUrl("/authenticate").usernameParameter("username").passwordParameter("password").and()

				.logout().logoutUrl("/logout").logoutSuccessUrl("/");

		List<String> publicEndpoints = Stream
				.concat(Arrays.stream(env.getProperty("kos.security.public", "").split(",")),
						Arrays.stream(env.getProperty("kos.security.public.extensions", "").split(",")))
				.collect(Collectors.toList());

		for (String publicEndpoint : publicEndpoints) {
			if (!publicEndpoint.equals(""))
				http.authorizeRequests().antMatchers(publicEndpoint).permitAll();
		}

		http.authorizeRequests().antMatchers("/**").access("isAuthenticated()").and().csrf().disable().httpBasic();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
