package com.cspinformatique.kubik.kos.domain.product.service;

import java.io.InputStream;

import com.cspinformatique.kubik.kos.model.product.Product;
import com.cspinformatique.kubik.kos.model.product.ProductImage;
import com.cspinformatique.kubik.kos.model.product.ProductImageSize;

public interface ProductImageService {
	ProductImage findByProductAndSize(Product product, ProductImageSize size);
	
	InputStream loadImageInputStream(Product product, ProductImageSize size);
}
