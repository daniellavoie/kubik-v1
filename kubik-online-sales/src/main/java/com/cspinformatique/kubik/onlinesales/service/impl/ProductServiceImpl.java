package com.cspinformatique.kubik.onlinesales.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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

		List<ProductImage> images = new ArrayList<>();
		for (com.cspinformatique.kubik.model.product.ProductImage kubikImage : kubikProduct.getImages()) {
			images.add(new ProductImage(0, product, ProductImageSize.valueOf(kubikImage.getSize().name())));
		}
		product.setImages(images);

		return product;

	}

	public Product save(Product product) {
		return productRepository.save(product);
	}
}
