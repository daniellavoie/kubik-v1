package com.cspinformatique.kubik.domain.company.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.company.Company;

public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {
	
}
