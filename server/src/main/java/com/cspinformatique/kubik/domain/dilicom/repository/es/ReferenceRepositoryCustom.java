package com.cspinformatique.kubik.domain.dilicom.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.dilicom.model.Reference;

public interface ReferenceRepositoryCustom {
	public Page<Reference> search(String query, Pageable pageable);
}
