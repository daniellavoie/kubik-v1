package com.cspinformatique.kubik.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Category;

public interface CategoryService {
	
	void delete(int id);
	
	List<Category> findAll();
	
	Page<Category> findAll(Pageable pageable);
	
	Category findOne(int id);
	
	Category save(Category category);
}
