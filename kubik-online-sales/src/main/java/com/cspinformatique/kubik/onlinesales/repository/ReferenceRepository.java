package com.cspinformatique.kubik.onlinesales.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.onlinesales.model.Reference;

public interface ReferenceRepository
		extends PagingAndSortingRepository<Reference, String>, ReferenceRepositoryCustom {
	Reference findByKubikId(int kubikId);
}
