package com.cspinformatique.kubik.onlinesales.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.onlinesales.model.Reference;

public interface ReferenceRepositoryCustom {
	Page<Reference> search(String query, Integer categoryId, String author, Date publishedFrom, Date publishedTo,
			String collection, Pageable pageable);
}
 