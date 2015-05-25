package com.cspinformatique.kubik.domain.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cspinformatique.kubik.model.user.User;

public interface UserService extends UserDetailsService {
	User findByUsername(String username);
	
	User findOne(int id);
	
	User save(User user);
}
