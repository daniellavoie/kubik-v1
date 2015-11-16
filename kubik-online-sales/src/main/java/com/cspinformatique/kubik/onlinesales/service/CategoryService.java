package com.cspinformatique.kubik.onlinesales.service;

import java.util.List;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.model.Category;

public interface CategoryService {	
	Category findByKubikId(int kubikId);
	
	Category findByName(String name);
	
	List<Category> findByRootCategory(boolean rootCategory);

	Category findOne(int id);
	
	List<Category> generateNestedCategories(int categoryId);

	void processKosNotification(KosNotification kosNotification);
}
