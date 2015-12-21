package com.cspinformatique.kubik.server.domain.product.service;

import java.io.InputStream;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductImage;
import com.cspinformatique.kubik.server.model.product.ProductImageSize;

public interface ProductImageService {
	ProductImage findByProductAndSize(Product product, ProductImageSize size);

	void initialLoad();
	
	InputStream loadInputStream(Product product, ProductImageSize size);

	void persistProductImagesToAws(Product product);
	
	void persistAmazonImages(Product product);
	
	void persistDilicomImages(Product product);
	
	void uploadImageToAws(byte[] imageBytes, Product product);
}
