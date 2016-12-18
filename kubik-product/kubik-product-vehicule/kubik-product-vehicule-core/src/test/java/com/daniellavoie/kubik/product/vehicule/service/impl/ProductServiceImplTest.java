package com.daniellavoie.kubik.product.vehicule.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daniellavoie.kubik.product.vehicule.model.Product;
import com.daniellavoie.kubik.product.vehicule.service.KubikService;
import com.daniellavoie.kubik.product.vehicule.service.ProductService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {
	@Autowired
	private ProductService productService;
	
	@MockBean
	private KubikService kubikService;

	@Test
	public void testRepository() {
		String id = "1";

		productService.delete(id);

		Assert.assertEquals("Sould not have found any result", 0,
				productService.query(null, new PageRequest(0, 100)).getTotalElements());

		Product product = new Product();
		product.setEan13(id);
		product.setName("Product 1");

		productService.save(product);

		product.setEan13("2");
		product.setName("Product 2");

		productService.save(product);

		Assert.assertEquals("Should have found two results.", 2l,
				productService.query(null, new PageRequest(0, 100)).getTotalElements());

		Assert.assertEquals("Should have found a single result for query 1.", 1l,
				productService.query("1", new PageRequest(0, 100)).getTotalElements());

		Assert.assertNotNull("Search by ean13 should have returned a result", productService.findOne("1"));
	}

	@After
	public void clean() {
		productService.delete("1");
		productService.delete("2");
	}
}
