package com.daniellavoie.kubik.product.vehicule.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daniellavoie.kubik.product.vehicule.model.Product;
import com.daniellavoie.kubik.product.vehicule.repository.ProductRepository;
import com.daniellavoie.kubik.product.vehicule.service.KubikService;
import com.daniellavoie.kubik.product.vehicule.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	private ProductRepository productRepository;
	private KubikService kubikService;

	public ProductServiceImpl(KubikService kubikService, ProductRepository productRepository) {
		this.kubikService = kubikService;
		this.productRepository = productRepository;
	}

	@Override
	public void delete(String ean13) {
		productRepository.delete(ean13);
	}

	@Override
	public Product findOne(String ean13) {
		return productRepository.findOne(ean13);
	}

	@Override
	public Page<Product> query(String queryString, Pageable pageable) {
		return productRepository.query(queryString, pageable);
	}

	@Override
	public Product save(Product product) {
		product.setId(kubikService.saveProduct(product));

		return productRepository.save(product);
	}
}
