package com.cspinformatique.kubik.kos.domain.product.service;

import java.util.List;

import com.cspinformatique.kubik.kos.model.product.Category;
import com.cspinformatique.kubik.server.model.kos.KosNotification;

public interface CategoryService {	
	Category findByKubikId(int kubikId);
	
	Category findByName(String name);
	
	List<Category> findByRootCategory(boolean rootCategory);

	Category findOne(int id);
	
	List<Category> generateNestedCategories(int categoryId);

	void processKosNotification(KosNotification kosNotification);
}
