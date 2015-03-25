package com.cspinformatique.kubik.domain.reference.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.reference.model.Reference;

public interface ReferenceRepositoryCustom {
	public Page<Reference> search(String query, String[] fields, Boolean importedInKubik, Pageable pageable);
}
