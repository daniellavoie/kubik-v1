package com.daniellavoie.kubik.product.vehicule.repository.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.daniellavoie.kubik.product.vehicule.model.Product;
import com.daniellavoie.kubik.product.vehicule.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProductRepositoryEsImpl implements ProductRepository {
	private String INDEX = "product-vehicule";
	private String TYPE = "v1";

	private Client client;
	private ObjectMapper objectMapper;

	public ProductRepositoryEsImpl(Client client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;

		try {
			client.admin().indices().prepareGetIndex().addIndices(INDEX).addTypes(TYPE).get();
		} catch (IndexNotFoundException indexNotFoundEx) {
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(
					new ClassPathResource("es/mappings/" + INDEX + "_" + TYPE + ".json").getInputStream()))) {
				if (!client.admin().indices().prepareCreate(INDEX).get().isAcknowledged())
					throw new RuntimeException("Index could not be created.");

				if (!client.admin().indices().preparePutMapping(INDEX).setType(TYPE)
						.setSource(buffer.lines().collect(Collectors.joining("\n"))).get().isAcknowledged())
					throw new RuntimeException("Mapping could not be created.");
			} catch (IOException ioEx) {
				throw new RuntimeException(ioEx);
			}
		}
	}

	@Override
	public void delete(String ean13) {
		client.prepareDelete(INDEX, "v1", ean13).get();
	}

	@Override
	public Product findOne(String ean13) {
		GetResponse response = client.prepareGet(INDEX, TYPE, ean13).get();

		try {
			return response.isExists() ? objectMapper
					.readValue(client.prepareGet(INDEX, TYPE, ean13).get().getSourceAsBytes(), Product.class) : null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> T map(SearchHit searchHit, Class<T> valueType) {
		try {
			return objectMapper.readValue(searchHit.getSourceRef().array(), valueType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Page<Product> query(String queryString, Pageable pageable) {
		BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
		if (queryString != null && !queryString.isEmpty())
			query = query.filter(QueryBuilders.queryStringQuery("_all:*" + queryString + "*"));

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX).setQuery(query)
				.setSize(pageable.getPageSize()).setFrom(pageable.getPageNumber());

		if (pageable.getSort() != null)
			for (Sort.Order order : pageable.getSort()) {
				searchRequestBuilder = searchRequestBuilder.addSort(order.getProperty(),
						order.isAscending() ? SortOrder.ASC : SortOrder.DESC);
			}

		SearchResponse searchResponse = searchRequestBuilder.get();

		return new PageImpl<>(Arrays.stream(searchResponse.getHits().getHits())
				.map(searchHit -> map(searchHit, Product.class)).collect(Collectors.toList()), pageable,
				searchResponse.getHits().getTotalHits());

	}

	@Override
	public Product save(Product product) {
		try {
			client.prepareIndex(INDEX, "v1").setSource(objectMapper.writeValueAsBytes(product))
					.setId(product.getEan13()).setRefresh(true).get();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		return product;
	}
}
