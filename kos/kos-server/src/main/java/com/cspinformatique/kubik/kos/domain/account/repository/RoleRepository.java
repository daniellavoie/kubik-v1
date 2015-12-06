package com.cspinformatique.kubik.kos.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.kos.model.account.Role;
import com.cspinformatique.kubik.kos.model.account.Role.Type;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByName(String name);
	
	Role findByType(Type type);
}
