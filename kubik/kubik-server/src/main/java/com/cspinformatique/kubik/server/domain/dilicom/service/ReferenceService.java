package com.cspinformatique.kubik.server.domain.dilicom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.model.product.Product;

public interface ReferenceService {
	Reference buildReferenceFromProduct(Product product);
	
	Iterable<Reference> cleanDoubles(Iterable<Reference> references);
	
	Product createProductFromReference(Reference reference);
	
	void delete(String id);
	
	void delete(String ean13, String supplierEan13);
	
	Reference find(String ean13, String supplierEan13, boolean importedInKubik);
	
	Iterable<Reference> findByEan13(String ean13, Sort sort);
	
	Iterable<Reference> findByEan13AndImportedInKubik(String ean13, boolean importedInKubik, Sort sort);

	Reference findByEan13AndSupplierEan13(String ean13, String supplierEan13);
	
	Page<Reference> findByImportedInKubik(boolean importedInubik, Pageable pageable);
	
	Reference findOne(String id);
	
	Reference save(Reference reference);
	
	Iterable<Reference> save(Iterable<Reference> references);
	
	Page<Reference> search(String query, Pageable pageable);
}
