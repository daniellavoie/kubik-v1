package com.cspinformatique.kubik.server.domain.company.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.company.repository.CompanyRepository;
import com.cspinformatique.kubik.server.domain.company.service.CompanyService;
import com.cspinformatique.kubik.server.model.company.Company;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Optional<Company> findComapny() {
		return this.companyRepository.findComapny();
	}

	@Override
	public Company save(Company company) {
		return companyRepository.save(company);
	}

}
