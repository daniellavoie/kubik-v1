package com.cspinformatique.livronet.dilicom.reference.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.livronet.dilicom.model.Reference;

public interface ReferenceRepository extends
		PagingAndSortingRepository<Reference, String> {

}
