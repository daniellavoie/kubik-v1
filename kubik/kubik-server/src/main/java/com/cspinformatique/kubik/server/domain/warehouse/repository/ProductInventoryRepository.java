package com.cspinformatique.kubik.server.domain.warehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.ProductInventory;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Integer> {
	Optional<ProductInventory> findByProduct(Product product);
	
	@Query("SELECT product.id FROM ProductInventory WHERE quantityOnHand > 0")
	List<Integer> findProductIdWithInventory();
}
