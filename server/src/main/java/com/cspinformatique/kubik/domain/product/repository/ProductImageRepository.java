package com.cspinformatique.kubik.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.ProductImage;
import com.cspinformatique.kubik.model.product.ProductImageSize;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	ProductImage findByProductAndSize(Product product, ProductImageSize size);
}
