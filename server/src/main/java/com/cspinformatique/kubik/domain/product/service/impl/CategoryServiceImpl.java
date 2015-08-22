package com.cspinformatique.kubik.domain.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.exception.CategoryNameAlreadyUsedException;
import com.cspinformatique.kubik.domain.product.repository.CategoryRepository;
import com.cspinformatique.kubik.domain.product.service.CategoryService;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.model.product.Category;
import com.cspinformatique.kubik.model.product.Product;

@Service
public class CategoryServiceImpl implements CategoryService {
	private static final String DEFAULT_NAME = "Nouvelle Catégorie";
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductService productService;

	private void assertNameNotAlreadyUsed(Category category) throws CategoryNameAlreadyUsedException {
		String name = category.getName();

		List<Category> existingCategory = categoryRepository.findByName(name);

		if (!existingCategory.isEmpty() && existingCategory.get(0).getId() != category.getId()) {
			throw new CategoryNameAlreadyUsedException(
					"Le nom " + category.getName() + " est déjà utilisé pour une autre catégorie.");
		}

	}

	@Override
	public void delete(int id) {
		categoryRepository.delete(id);
	}

	@Override
	public void deleteProductCategories(Category category) {
		for(Product product : productService.findByCategory(category)){
			product.setCategory(null);
			
			productService.save(product);
		}
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	@Override
	public List<Category> findByRootCategory(boolean rootCategory) {
		return categoryRepository.findByRootCategory(true);
	}

	@Override
	public Category findOne(int id) {
		return categoryRepository.findOne(id);
	}

	@Override
	public String generateNewName() {
		String name = null;
		int attempt = 1;
		do {
			name = DEFAULT_NAME;

			if (attempt != 1) {
				name += " " + attempt;
			}

			List<Category> category = categoryRepository.findByName(name);

			if (!category.isEmpty()) {
				name = null;
			}

			++attempt;
		} while (name == null);

		return name;
	}

	@Override
	public Category save(Category category) {
		assertNameNotAlreadyUsed(category);

		return categoryRepository.save(category);
	}
}
