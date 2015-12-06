package com.cspinformatique.kubik.server.domain.sales.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.sales.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

	Customer findByEmail(String email);

	@Query("SELECT customer FROM Customer customer WHERE CONCAT(firstName, ' ', lastName) like ?1 OR email like ?1")
	Page<Customer> search(String query, Pageable pageable);
}
