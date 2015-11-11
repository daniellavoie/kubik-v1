package com.cspinformatique.kubik.onlinesales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.onlinesales.model.Product;
import com.cspinformatique.kubik.onlinesales.model.ProductImage;
import com.cspinformatique.kubik.onlinesales.model.ProductImageSize;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	ProductImage findByProductAndSize(Product product, ProductImageSize size);
}
