package com.cspinformatique.kubik.onlinesales.service;

import java.io.InputStream;

import com.cspinformatique.kubik.onlinesales.model.Product;
import com.cspinformatique.kubik.onlinesales.model.ProductImage;
import com.cspinformatique.kubik.onlinesales.model.ProductImageSize;

public interface ProductImageService {
	ProductImage findByProductAndSize(Product product, ProductImageSize size);
	
	InputStream loadImageInputStream(Product product, ProductImageSize size);
}
