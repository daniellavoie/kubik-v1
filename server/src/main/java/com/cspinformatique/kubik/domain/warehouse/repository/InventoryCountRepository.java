package com.cspinformatique.kubik.domain.warehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.warehouse.InventoryCount;

public interface InventoryCountRepository extends JpaRepository<InventoryCount, Long> {
	Page<InventoryCount> findByProduct(Product product, Pageable pageable);

	@Query("SELECT sum(quantity) FROM InventoryCount WHERE product.id = ?1")
	double findProductQuantityCounted(int productId);
}
