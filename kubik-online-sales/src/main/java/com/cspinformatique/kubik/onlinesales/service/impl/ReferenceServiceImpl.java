package com.cspinformatique.kubik.onlinesales.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.onlinesales.model.Reference;
import com.cspinformatique.kubik.onlinesales.repository.ReferenceRepository;
import com.cspinformatique.kubik.onlinesales.service.ReferenceService;

@Service
public class ReferenceServiceImpl implements ReferenceService {
	@Resource
	private ReferenceRepository referenceRepository;
	
	@Override
	public Reference findByKubikId(int kubikId) {
		return referenceRepository.findByKubikId(kubikId);
	}

	@Override
	public Reference save(Reference reference) {
		return referenceRepository.save(reference);
	}

	@Override
	public Page<Reference> search(String query, Integer categoryId, String author, Date publishedFrom,
			Date publishedTo, String collection) {
//		return referenceRepository.search(query, categoryId, author, publishedFrom, publishedTo, collection,
//				new PageRequest(0, 100));
		return null;
	}
}
