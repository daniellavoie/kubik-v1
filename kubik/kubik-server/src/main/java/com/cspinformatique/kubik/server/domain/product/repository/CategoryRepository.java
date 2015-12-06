package com.cspinformatique.kubik.server.domain.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findByName(String name);
	
	List<Category> findByRootCategory(boolean rootCategory);
}
