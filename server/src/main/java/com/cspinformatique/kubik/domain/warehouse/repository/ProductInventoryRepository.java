package com.cspinformatique.kubik.domain.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.warehouse.ProductInventory;

public interface ProductInventoryRepository extends PagingAndSortingRepository<ProductInventory, Integer> {
	ProductInventory findByProduct(Product product);

	@Query("SELECT product.id FROM ProductInventory WHERE quantityOnHand > 0")
	List<Integer> findProductIdWithInventory();
}
