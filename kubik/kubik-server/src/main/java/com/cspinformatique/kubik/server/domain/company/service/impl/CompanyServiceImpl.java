package com.cspinformatique.kubik.server.domain.company.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.company.repository.CompanyRepository;
import com.cspinformatique.kubik.server.domain.company.service.CompanyService;
import com.cspinformatique.kubik.server.model.company.Company;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired private CompanyRepository companyRepository;
	
	@Override
	public Company find() {
		return this.companyRepository.findAll(new PageRequest(0, 1)).getContent().get(0);
	}

}
