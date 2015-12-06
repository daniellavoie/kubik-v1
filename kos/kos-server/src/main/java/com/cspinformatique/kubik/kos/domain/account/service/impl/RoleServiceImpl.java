package com.cspinformatique.kubik.kos.domain.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.account.repository.RoleRepository;
import com.cspinformatique.kubik.kos.domain.account.service.RoleService;
import com.cspinformatique.kubik.kos.model.account.Role;
import com.cspinformatique.kubik.kos.model.account.Role.Type;

@Service
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleRepository roleRepository;

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Role findByType(Type type) {
		return roleRepository.findByType(type);
	}

	@Override
	public Role findOne(int id) {
		return roleRepository.findOne(id);
	}

}
