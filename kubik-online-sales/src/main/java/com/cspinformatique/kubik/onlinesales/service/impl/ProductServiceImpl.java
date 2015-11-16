package com.cspinformatique.kubik.onlinesales.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.model.Category;
import com.cspinformatique.kubik.onlinesales.model.Product;
import com.cspinformatique.kubik.onlinesales.model.ProductImage;
import com.cspinformatique.kubik.onlinesales.model.ProductImageSize;
import com.cspinformatique.kubik.onlinesales.repository.ProductRepository;
import com.cspinformatique.kubik.onlinesales.rest.KubikTemplate;
import com.cspinformatique.kubik.onlinesales.service.CategoryService;
import com.cspinformatique.kubik.onlinesales.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Resource
	private KubikTemplate kubikTemplate;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductRepository productRepository;

	@Override
	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public Product findOne(int id) {
		return productRepository.findOne(id);
	}

	private List<Category> calculateCategoriesScope(List<Integer> categoriesIds) {
		List<Category> categories = categoriesIds != null ? categoriesIds.stream().map(categoryId -> {
			Category category = categoryService.findOne(categoryId);
			if (category == null) {
				LOGGER.warn("Category " + categoryId + " does not exists. Will be filtered from product search.");
			}
			return category;
		}).filter(category -> category != null).collect(Collectors.toList()) : null;

		HashMap<Integer, Category> categoriesMap = new HashMap<>();

		categories.forEach(category -> {
			categoriesMap.put(category.getId(), category);
			addChildCategoriesToMap(categoriesMap, category);
		});

		return new ArrayList<Category>(categoriesMap.values());
	}

	private void addChildCategoriesToMap(Map<Integer, Category> categoriesMap, Category category) {
		category.getChildCategories().forEach(childCategory -> {
			categoriesMap.put(childCategory.getId(), childCategory);

			addChildCategoriesToMap(categoriesMap, childCategory);
		});
	}

	@Override
	public void processKosNotification(KosNotification kosNotification) {
		Product product = requestProductFromKubik(kosNotification.getKubikId());

		save(product);
	}

	private Product requestProductFromKubik(int productId) {
		com.cspinformatique.kubik.model.product.Product kubikProduct;

		kubikProduct = kubikTemplate.exchange("/product/" + productId, HttpMethod.GET,
				com.cspinformatique.kubik.model.product.Product.class).getBody();

		// Checks if the product id already exists.
		Product product = productRepository.findByKubikId(productId);

		if (product == null) {
			product = new Product();
			product.setKubikId(productId);

			if (kubikProduct.getCategory() != null) {
				// Loads the category
				int categoryId = kubikProduct.getCategory().getId();
				Category category = categoryService.findByKubikId(categoryId);

				if (category == null) {
					throw new RuntimeException("Category with Kubik id " + categoryId
							+ " doesn not exists. Product will not be synchronized.");
				}
				product.setCategory(category);
			}
		}

		product.setAuthor(kubikProduct.getAuthor());
		product.setCollection(kubikProduct.getCollection());
		product.setDatePublished(kubikProduct.getDatePublished());
		product.setIsbn(kubikProduct.getIsbn());
		product.setManufacturer(kubikProduct.getPublisher());
		product.setPrice(kubikProduct.getPriceTaxIn());
		product.setTitle(kubikProduct.getExtendedLabel());

		product.getImages().clear();
		for (com.cspinformatique.kubik.model.product.ProductImage kubikImage : kubikProduct.getImages()) {
			product.getImages().add(new ProductImage(0, product, ProductImageSize.valueOf(kubikImage.getSize().name()),
					kubikImage.getContentLength()));
		}

		return product;

	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Page<Product> search(String title, String author, List<Integer> categoriesIds, Date publishFrom,
			Date publishUntil, String manufacturer, Double priceFrom, Double priceTo, String query, Pageable pageable) {
		if (title != null)
			if (title.equals(""))
				title = null;
			else
				title = "%" + title + "%";
		if (author != null)
			if (author.equals(""))
				author = null;
			else
				author = "%" + author + "%";
		if (manufacturer != null)
			if (manufacturer.equals(""))
				manufacturer = null;
			else
				manufacturer = "%" + manufacturer + "%";
		if (query != null)
			if (query.equals(""))
				query = null;
			else
				query = "%" + query + "%";

		if (categoriesIds == null)
			return productRepository.search(title, author, publishFrom, publishUntil, manufacturer, priceFrom, priceTo,
					query, pageable);
		else
			return productRepository.search(title, author, calculateCategoriesScope(categoriesIds), publishFrom,
					publishUntil, manufacturer, priceFrom, priceTo, query, pageable);
	}
}
