package com.cspinformatique.kubik.server.domain.dilicom.repository.es.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.domain.dilicom.repository.es.ReferenceRepositoryCustom;

@Repository
public class ReferenceRepositoryImpl implements ReferenceRepositoryCustom {
	public static String[] SEARCH_COLUMNS = { "ean13", "extendedLabel",
			"publisher", "collection", "author", "isbn" };

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public Page<Reference> search(String query, Pageable pageable) {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (query != null && !query.equals("")) {
			nativeSearchQueryBuilder.withQuery(new BoolQueryBuilder()
					.should(new MatchQueryBuilder("ean13", query))
					.should(new MatchQueryBuilder("extendedLabel", query))
					.should(new MatchQueryBuilder("publisher", query))
					.should(new MatchQueryBuilder("collection", query))
					.should(new MatchQueryBuilder("author", query))
					.should(new MatchQueryBuilder("isbn", query)));
		}

		return elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder
				.withPageable(pageable).build(), Reference.class);
	}
}
