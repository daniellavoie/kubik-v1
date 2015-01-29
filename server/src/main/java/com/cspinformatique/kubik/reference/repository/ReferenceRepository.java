package com.cspinformatique.kubik.reference.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.reference.model.Reference;

public interface ReferenceRepository extends
		PagingAndSortingRepository<Reference, String>, ReferenceRepositoryCustom {
	Iterable<Reference> findByEan13(String ean13, Sort sort);
	
	Iterable<Reference> findByEan13AndImportedInKubik(String ean13, boolean importedInKubik, Sort sort);
	
	List<Reference> findByEan13AndSupplierEan13(String ean13, String supplierEan13);
	
	Page<Reference> findByImportedInKubik(boolean importedInubik, Pageable pageable);
}
