package com.cspinformatique.kubik.domain.product.service;

import java.io.InputStream;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.ProductImage;
import com.cspinformatique.kubik.model.product.ProductImageSize;

public interface ProductImageService {
	ProductImage findByProductAndSize(Product product, ProductImageSize size);

	void initialLoad();
	
	InputStream loadInputStream(Product product, ProductImageSize size);

	void persistProductImagesToAws(int productId);
}
