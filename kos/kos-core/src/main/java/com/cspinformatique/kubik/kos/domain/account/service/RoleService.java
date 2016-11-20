package com.cspinformatique.kubik.kos.domain.account.service;

import com.cspinformatique.kubik.kos.model.account.Role;
import com.cspinformatique.kubik.kos.model.account.Role.Type;

public interface RoleService {
	Role findByName(String name);

	Role findByType(Type type);

	Role findOne(int id);
}
