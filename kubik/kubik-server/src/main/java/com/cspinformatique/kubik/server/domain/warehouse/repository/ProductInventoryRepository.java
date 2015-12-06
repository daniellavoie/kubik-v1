package com.cspinformatique.kubik.server.domain.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.ProductInventory;

public interface ProductInventoryRepository extends PagingAndSortingRepository<ProductInventory, Integer> {
	ProductInventory findByProduct(Product product);
	
	@Query("SELECT product.id FROM ProductInventory WHERE quantityOnHand > 0")
	List<Integer> findProductIdWithInventory();
}
