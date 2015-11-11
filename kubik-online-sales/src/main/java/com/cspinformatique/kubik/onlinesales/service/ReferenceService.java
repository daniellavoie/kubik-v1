package com.cspinformatique.kubik.onlinesales.service;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.cspinformatique.kubik.onlinesales.model.Reference;

public interface ReferenceService {
	Reference findByKubikId(int kubikId);
	
	Reference save(Reference reference);
	
	Page<Reference> search(String query, Integer categoryId, String author, Date publishedFrom, Date publishedTo, String collection);
}
