package com.cspinformatique.kubik.server.domain.company.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.company.Company;

public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {
	
}
