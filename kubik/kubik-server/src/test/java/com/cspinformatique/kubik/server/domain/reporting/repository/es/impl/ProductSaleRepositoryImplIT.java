package com.cspinformatique.kubik.server.domain.reporting.repository.es.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.mockito.Mockito;

import com.cspinformatique.kubik.server.domain.reporting.model.ProductSale;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductSaleRepositoryImplIT {
	private ProductSaleRepositoryImpl productSaleRepository;

	// @Before
	public void init() {
		Client client = Mockito.mock(Client.class);
		productSaleRepository = new ProductSaleRepositoryImpl(client, new ObjectMapper());
	}

	// @Test
	public void testProductSaleRepository() {
		Client client = Mockito.mock(Client.class);

		ProductSale productSale = new ProductSale(1, "111", new Date(), "Test product",
				Arrays.asList(new String[] { "Roman", "SF" }), 10.99d, 1, 5.5d);

		productSaleRepository.save(Arrays.asList(new ProductSale[] { productSale }));

		Date fromDate = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
		Date toDate = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

		Assert.assertTrue(
				client.prepareSearch("product-sale")
						.setQuery(QueryBuilders.boolQuery()
								.filter(QueryBuilders.rangeQuery("date").from(fromDate).to(toDate)))
						.get().getHits().getTotalHits() == 1);
	}
}
