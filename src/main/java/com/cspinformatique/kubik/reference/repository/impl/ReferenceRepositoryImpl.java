package com.cspinformatique.kubik.reference.repository.impl;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import com.cspinformatique.kubik.reference.model.Reference;
import com.cspinformatique.kubik.reference.repository.ReferenceRepositoryCustom;

@Repository
public class ReferenceRepositoryImpl implements ReferenceRepositoryCustom {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public Page<Reference> search(String query,
			String[] fields, Boolean importedInKubik,
			Pageable pageable) {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		
		if(query != null && !query.equals("")){
			QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(
					query);
	
			for (String field : fields) {
				queryBuilder.field(field);
			}
			
			nativeSearchQueryBuilder.withQuery(queryBuilder);
		}
		
		if(importedInKubik != null){
			nativeSearchQueryBuilder.withFilter(FilterBuilders.boolFilter()
					.must(FilterBuilders.termFilter("importedInKubik", importedInKubik)));
		}

		return elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder
				.withPageable(pageable).build(), Reference.class);
	}

}
