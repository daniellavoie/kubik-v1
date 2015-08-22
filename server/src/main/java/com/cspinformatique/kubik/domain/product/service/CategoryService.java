package com.cspinformatique.kubik.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Category;

public interface CategoryService {

	void delete(int id);

	void deleteProductCategories(Category category);

	List<Category> findAll();

	Page<Category> findAll(Pageable pageable);

	List<Category> findByRootCategory(boolean rootCategory);

	Category findOne(int id);

	String generateNewName();

	Category save(Category category);
}
