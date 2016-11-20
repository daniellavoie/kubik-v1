package com.cspinformatique.kubik.kos.domain.product.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.kos.model.product.Category;
import com.cspinformatique.kubik.kos.model.product.Product;
import com.cspinformatique.kubik.server.model.kos.KosNotification;

public interface ProductService {
	List<Product> findByCategory(Category category);

	Product findOne(int id);

	void processProductNotification(KosNotification kosNotification);

	void processProductInventoryNotification(KosNotification kosNotification);

	Product save(Product product);

	Page<Product> search(String title, String author, List<String> categories, Date publishFrom, Date publishUntil,
			String manufacturer, Double priceFrom, Double priceTo, Boolean hideUnavailable, String query, Pageable pageable);
}