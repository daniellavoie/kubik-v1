package com.cspinformatique.kubik.server.domain.company.service;

import java.util.Optional;

import com.cspinformatique.kubik.server.model.company.Company;

public interface CompanyService {
	Optional<Company> findComapny();
	
	Company save(Company company);
}
