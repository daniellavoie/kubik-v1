package com.cspinformatique.kubik.server.domain.dilicom.repository.es.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.domain.dilicom.repository.es.ReferenceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ReferenceRepositoryImpl implements ReferenceRepository {
	public static String[] SEARCH_COLUMNS = { "ean13", "extendedLabel", "publisher", "collection", "author", "isbn" };

	private Client client;
	private ObjectMapper objectMapper;

	@Autowired
	public ReferenceRepositoryImpl(Client client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;

		createMappingIfNotExist();
	}

	private void addPageableToBuilder(Pageable pageable, SearchRequestBuilder builder) {
		builder.setSize(pageable.getPageSize()).setFrom(pageable.getPageNumber());

		if (pageable.getSort() != null)
			StreamSupport.stream(pageable.getSort().spliterator(), false)
					.collect(Collectors.toMap(order -> order.getProperty(), order -> order.getDirection().name()))
					.entrySet().forEach(entry -> builder.addSort(entry.getKey(), SortOrder.valueOf(entry.getValue())));
	}

	private void createMappingIfNotExist() {
		if (!client.admin().indices().prepareExists("reference").execute().actionGet().isExists()) {
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(
					new ClassPathResource("es-mappings/reference-mapping.json").getInputStream()))) {
				if (!client.admin().indices().prepareCreate("reference")
						.addMapping("reference", buffer.lines().collect(Collectors.joining("\n"))).get()
						.isAcknowledged()) {
					throw new RuntimeException("Mapping of type reference for index refernece could not be created.");
				}
			} catch (IOException ioEx) {
				throw new RuntimeException(ioEx);
			}
		}
	}

	public boolean delete(String id) {
		return client.prepareDelete("reference", "reference", id).setRefreshPolicy(RefreshPolicy.WAIT_UNTIL).get()
				.getResult().equals(Result.DELETED);
	}

	@Override
	public List<Reference> findByEan13(String ean13) {
		return toList(client.prepareSearch("reference")
				.setQuery(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("ean13", ean13))).get());
	}

	@Override
	public List<Reference> findByEan13AndImportedInKubik(String ean13, boolean importedInKubik) {
		return toList(
				client.prepareSearch("reference")
						.setQuery(
								QueryBuilders.boolQuery()
										.filter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("ean13", ean13))
												.must(QueryBuilders.termQuery("importedInKubik", importedInKubik))))
						.get());
	}

	@Override
	public List<Reference> findByEan13AndSupplierEan13(String ean13, String supplierEan13) {
		return toList(client.prepareSearch("reference")
				.setQuery(QueryBuilders.boolQuery()
						.filter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("ean13", ean13))
								.must(QueryBuilders.termQuery("supplierEan13", supplierEan13))))
				.get());
	}

	@Override
	public Optional<Reference> findByEan13AndSupplierEan13AndImportedInKubik(String ean13, String supplierEan13,
			boolean importedInKubik) {
		List<Reference> references = toList(client.prepareSearch("reference")
				.setQuery(QueryBuilders.boolQuery()
						.filter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("ean13", ean13))
								.must(QueryBuilders.termQuery("supplierEan13", supplierEan13))
								.must(QueryBuilders.termQuery("importedInKubik", importedInKubik))))
				.get());

		return references.size() > 0 ? Optional.of(references.get(0)) : Optional.empty();
	}

	@Override
	public Page<Reference> findByImportedInKubik(boolean importedInKubik, Pageable pageable) {
		SearchRequestBuilder builder = client.prepareSearch("reference")
				.setQuery(QueryBuilders.termQuery("importedInKubik", importedInKubik));

		addPageableToBuilder(pageable, builder);

		SearchResponse searchResponse = builder.get();

		return new PageImpl<Reference>(toList(searchResponse), pageable, searchResponse.getHits().getTotalHits());
	}

	@Override
	public Optional<Reference> findOne(String id) {
		GetResponse getResponse = client.prepareGet("reference", "reference", id).get();

		try {
			if (getResponse.isExists()) {
				Reference reference = objectMapper.readValue(getResponse.getSourceAsBytes(), Reference.class);
				reference.setId(id);

				return Optional.of(reference);
			} else {
				return Optional.empty();
			}
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private Reference map(SearchHit hit) {
		try {
			Reference reference = objectMapper.readValue(BytesReference.toBytes(hit.getSourceRef()), Reference.class);

			reference.setId(hit.getId());

			return reference;
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	public void save(List<Reference> references) {
		BulkRequestBuilder builder = client.prepareBulk().setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);

		references.forEach(reference -> {
			IndexRequestBuilder indexBuilder = client.prepareIndex("reference", "reference");
			if (reference.getId() != null)
				indexBuilder.setId(reference.getId());

			try {
				indexBuilder.setSource(objectMapper.writeValueAsBytes(reference));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}

			builder.add(indexBuilder);
		});

		BulkResponse bulkResponse = builder.get();
		if (StreamSupport.stream(bulkResponse.spliterator(), false).filter(itemResponse -> itemResponse.isFailed())
				.findAny().isPresent()) {
			throw new RuntimeException(bulkResponse.buildFailureMessage());
		}
	}

	@Override
	public Page<Reference> search(String query, Pageable pageable) {

		SearchRequestBuilder builder = client.prepareSearch("reference").setQuery(query != null && !query.equals("")
				? QueryBuilders.multiMatchQuery(query, "ean13", "extendedLabel", "publisher", "collection", "author")
				: QueryBuilders.matchAllQuery());

		addPageableToBuilder(pageable, builder);

		SearchResponse searchResponse = builder.get();

		return new PageImpl<Reference>(toList(searchResponse), pageable, searchResponse.getHits().getTotalHits());
	}

	private List<Reference> toList(SearchResponse searchResponse) {
		return StreamSupport.stream(searchResponse.getHits().spliterator(), false).map(this::map)
				.collect(Collectors.toList());
	}
}
