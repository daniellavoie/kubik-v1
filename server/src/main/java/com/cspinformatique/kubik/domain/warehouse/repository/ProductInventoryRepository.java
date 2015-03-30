package com.cspinformatique.kubik.domain.warehouse.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.warehouse.ProductInventory;

public interface ProductInventoryRepository extends
		PagingAndSortingRepository<ProductInventory, Integer> {
	ProductInventory findByProduct(Product product);
}
