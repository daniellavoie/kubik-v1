package com.cspinformatique.kubik.domain.product.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.product.Supplier;

public interface SupplierRepository extends
		PagingAndSortingRepository<Supplier, Integer> {
	public Supplier findByEan13(String ean13);
}
