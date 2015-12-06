package com.cspinformatique.kubik.server.domain.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cspinformatique.kubik.server.model.user.User;

public interface UserService extends UserDetailsService {
	User findByUsername(String username);
	
	User findOne(int id);
	
	User save(User user);
}
