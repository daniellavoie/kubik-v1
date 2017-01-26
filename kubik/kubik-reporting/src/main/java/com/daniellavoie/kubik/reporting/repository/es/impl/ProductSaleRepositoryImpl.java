package com.daniellavoie.kubik.reporting.repository.es.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.daniellavoie.kubik.reporting.model.ProductSale;
import com.daniellavoie.kubik.reporting.repository.es.ProductSaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProductSaleRepositoryImpl implements ProductSaleRepository {
	private Client client;
	private ObjectMapper objectMapper;

	public ProductSaleRepositoryImpl(Client client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;

		createMappingIfNotExist();
	}

	private void createMappingIfNotExist() {
		client.admin().cluster().prepareHealth("product-sale").setWaitForYellowStatus().get();

		if (!client.admin().indices().prepareExists("product-sale").execute().actionGet().isExists()) {
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(
					new ClassPathResource("es-mappings/product-sale-mapping.json").getInputStream()))) {
				if (!client.admin().indices().prepareCreate("product-sale")
						.addMapping("product-sale", buffer.lines().collect(Collectors.joining("\n"))).get()
						.isAcknowledged()) {
					throw new RuntimeException(
							"Mapping of type product-sale for index product-sale could not be created.");
				}
			} catch (IOException ioEx) {
				throw new RuntimeException(ioEx);
			}
		}
	}

	@Override
	public List<ProductSale> findAll() {
		try {
			return Arrays.stream(client.prepareSearch("product-sale").execute().get().getHits().hits()).map(hit -> {
				try {
					return objectMapper.readValue(hit.getSourceAsString(), ProductSale.class);
				} catch (IOException ioEx) {
					throw new RuntimeException(ioEx);
				}
			}).collect(Collectors.toList());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void save(List<ProductSale> productSales) {
		BulkRequestBuilder builder = client.prepareBulk().setRefresh(true);

		productSales.forEach(productSale -> {
			productSale.setAmount(productSale.getAmount() * 2);
			productSale.setQuantity(productSale.getQuantity() * 2);
			IndexRequestBuilder indexBuilder = client.prepareIndex("product-sale", "product-sale",
					String.valueOf(productSale.getInvoiceDetailId()));
			try {
				indexBuilder.setSource(objectMapper.writeValueAsBytes(productSale));
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

}
