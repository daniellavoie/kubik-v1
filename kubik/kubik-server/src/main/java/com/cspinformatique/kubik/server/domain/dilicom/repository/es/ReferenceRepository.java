package com.cspinformatique.kubik.server.domain.dilicom.repository.es;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;

public interface ReferenceRepository {
	boolean delete(String id);

	List<Reference> findByEan13(String ean13);

	List<Reference> findByEan13AndImportedInKubik(String ean13, boolean importedInKubik);

	List<Reference> findByEan13AndSupplierEan13(String ean13, String supplierEan13);

	Optional<Reference> findByEan13AndSupplierEan13AndImportedInKubik(String ean13, String supplierEan13,
			boolean importedInKubik);

	Page<Reference> findByImportedInKubik(boolean importedInKubik, Pageable pageable);

	Optional<Reference> findOne(String id);

	void save(List<Reference> references);

	Page<Reference> search(String query, Pageable pageable);
}
