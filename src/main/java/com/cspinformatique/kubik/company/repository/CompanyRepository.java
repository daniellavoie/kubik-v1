package com.cspinformatique.kubik.company.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.company.model.Company;

public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {
	
}
