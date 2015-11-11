package com.cspinformatique.kubik.onlinesales.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.model.Category;
import com.cspinformatique.kubik.onlinesales.repository.CategoryRepository;
import com.cspinformatique.kubik.onlinesales.rest.KubikTemplate;
import com.cspinformatique.kubik.onlinesales.service.CategoryService;
import com.cspinformatique.kubik.onlinesales.service.ProductService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryRepository categoryRepository;

	@Resource
	private ProductService productService;

	@Resource
	private KubikTemplate kubikTemplate;

	@Override
	public Category findByKubikId(int kubikId) {
		return categoryRepository.findByKubikId(kubikId);
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
	public void processKosNotification(KosNotification kosNotification) {
		transcodeFromKubik(kubikTemplate.exchange("/category/" + kosNotification.getKubikId(), HttpMethod.GET,
				com.cspinformatique.kubik.model.product.Category.class).getBody());
	}

	private Category transcodeFromKubik(com.cspinformatique.kubik.model.product.Category kubikCategory) {
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

		return save(category);
	}

	private Category save(Category category) {
		category.getChildCategories().forEach(childCategory -> childCategory.setParentCategory(category));

		return categoryRepository.save(category);
	}
}
