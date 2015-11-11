package com.cspinformatique.kubik.onlinesales.service;

import java.util.List;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.model.Category;
import com.cspinformatique.kubik.onlinesales.model.Product;

public interface ProductService {
	List<Product> findByCategory(Category category);
	
	Product findOne(int id);
	
	void processKosNotification(KosNotification kosNotification);
	
	Product save(Product product);
}
