package com.cspinformatique.kubik.server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
}
