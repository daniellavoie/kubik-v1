package com.cspinformatique.kubik.onlinesales.repository.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import com.cspinformatique.kubik.onlinesales.model.Reference;
import com.cspinformatique.kubik.onlinesales.repository.ReferenceRepositoryCustom;

@Repository
public class ReferenceRepositoryImpl implements ReferenceRepositoryCustom {
	@Resource
	private ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public Page<Reference> search(String query, Integer categoryId, String author, Date publishedFrom,
			Date publishedTo, String collection, Pageable pageable) {
		return null;
	}

}
