package com.cspinformatique.kubik.kos.domain.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.product.repository.CategoryRepository;
import com.cspinformatique.kubik.kos.domain.product.service.CategoryService;
import com.cspinformatique.kubik.kos.domain.product.service.ProductService;
import com.cspinformatique.kubik.kos.model.product.Category;
import com.cspinformatique.kubik.kos.rest.KubikTemplate;
import com.cspinformatique.kubik.server.model.kos.KosNotification;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryRepository categoryRepository;

	@Resource
	private ProductService productService;

	@Resource
	private KubikTemplate kubikTemplate;

	private void addChildCategoriesToList(Category category, List<Category> categoriesList) {
		categoriesList.addAll(category.getChildCategories());

		category.getChildCategories().forEach(childCategory -> addChildCategoriesToList(childCategory, categoriesList));
	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public Category findByKubikId(int kubikId) {
		return categoryRepository.findByKubikId(kubikId);
	}

	@Override
	public List<Category> findByRootCategory(boolean rootCategory) {
		return categoryRepository.findByRootCategory(rootCategory);
	}

	@Override
	public List<Category> generateNestedCategories(int categoryId) {
		Category category = categoryRepository.findOne(categoryId);

		List<Category> categoriesList = new ArrayList<>();
		categoriesList.add(category);

		addChildCategoriesToList(category, categoriesList);

		return categoriesList;
	}

	private void deleteCategory(Category category) {
		// Delete child categories.
		category.getChildCategories().forEach(childCategory -> deleteCategory(category));

		// Remove category from product using it.
		productService.findByCategory(category).forEach(product -> {
			product.setCategory(null);
			productService.save(product);
		});

		// Delete the category.
		categoryRepository.delete(category);
	}

	@Override
	public Category findOne(int id) {
		return categoryRepository.findOne(id);
	}

	@Override
	public void processKosNotification(KosNotification kosNotification) {
		transcodeFromKubik(kubikTemplate.exchange("/category/" + kosNotification.getKubikId(), HttpMethod.GET,
				com.cspinformatique.kubik.server.model.product.Category.class).getBody());
	}

	private Category transcodeFromKubik(com.cspinformatique.kubik.server.model.product.Category kubikCategory) {
		// Checks if the category already exists.
		Category category = categoryRepository.findByKubikId(kubikCategory.getId());
		if (category != null) {
			// Checks if some categories needs to be deleted.
			category.getChildCategories().stream().forEach(existingChildCategory -> {
				if (!kubikCategory.getChildCategories().stream().anyMatch(
						(newChildCategory -> newChildCategory.getId() == existingChildCategory.getKubikId()))) {
					deleteCategory(existingChildCategory);
				}
			});
		} else {
			category = new Category();
			category.setKubikId(kubikCategory.getId());
		}

		category.setName(kubikCategory.getName());
		category.setRootCategory(kubikCategory.isRootCategory());

		List<Category> childCategories = new ArrayList<>();
		kubikCategory.getChildCategories()
				.forEach(childCategory -> childCategories.add(transcodeFromKubik(childCategory)));
		category.setChildCategories(childCategories);
		category.setAvailableOnline(kubikCategory.isAvailableOnline());

		return save(category);
	}

	private Category save(Category category) {
		category.getChildCategories().forEach(childCategory -> childCategory.setParentCategory(category));

		return categoryRepository.save(category);
	}
}
