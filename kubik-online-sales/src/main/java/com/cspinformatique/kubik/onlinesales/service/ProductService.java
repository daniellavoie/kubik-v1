package com.cspinformatique.kubik.onlinesales.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.model.Category;
import com.cspinformatique.kubik.onlinesales.model.Product;

public interface ProductService {
	List<Product> findByCategory(Category category);
	
	Product findOne(int id);
	
	void processKosNotification(KosNotification kosNotification);
	
	Product save(Product product);
	
	Page<Product> search(String title, String author, List<Integer> categories, Date publishFrom, Date publishUntil, String manufacturer,
			Double priceFrom, Double priceTo, String query, Pageable pageable);
}
