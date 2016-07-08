package com.cspinformatique.kubik.server.domain.reporting.repository.es.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cspinformatique.kubik.server.domain.reporting.model.ProductSale;
import com.cspinformatique.kubik.server.util.AbstractElasticsearchIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductSaleRepositoryImplIT extends AbstractElasticsearchIntegrationTest {
	private ProductSaleRepositoryImpl productSaleRepository;

	@Before
	public void init() {
		productSaleRepository = new ProductSaleRepositoryImpl(getClient(), new ObjectMapper());
	}

	@Test
	public void testProductSaleRepository() {
		ProductSale productSale = new ProductSale(1, "111", new Date(), "Test product",
				Arrays.asList(new String[] { "Roman", "SF" }), 10.99d, 1, 5.5d);

		productSaleRepository.save(Arrays.asList(new ProductSale[] { productSale }));

		Date fromDate = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
		Date toDate = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

		Assert.assertTrue(
				getClient().prepareSearch("product-sale")
						.setQuery(QueryBuilders.boolQuery()
								.filter(QueryBuilders.rangeQuery("date").from(fromDate).to(toDate)))
						.get().getHits().getTotalHits() == 1);
	}
}
