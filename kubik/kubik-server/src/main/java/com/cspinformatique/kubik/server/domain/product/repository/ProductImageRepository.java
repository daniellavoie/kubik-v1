package com.cspinformatique.kubik.server.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductImage;
import com.cspinformatique.kubik.server.model.product.ProductImageSize;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	void deleteByProduct(Product product);
	
	ProductImage findByProductAndSize(Product product, ProductImageSize size);
}
