package com.cspinformatique.kubik.server.domain.company.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.company.Company;

public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {
	@Query("SELECT company FROM Company company")
	Optional<Company> findComapny();
}
