package com.cspinformatique.kubik.onlinesales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.onlinesales.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	void delete(Category category);

	Category findByName(String name);
	
	Category findByKubikId(int kubikId);
	
	List<Category> findByRootCategory(boolean rootCategory);
}
