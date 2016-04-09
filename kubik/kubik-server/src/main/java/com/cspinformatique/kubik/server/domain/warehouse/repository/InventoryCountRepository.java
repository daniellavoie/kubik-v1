package com.cspinformatique.kubik.server.domain.warehouse.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.InventoryCount;

public interface InventoryCountRepository extends JpaRepository<InventoryCount, Long> {

	List<InventoryCount> findByDateCountedAfter(Date dateCounted);

	List<InventoryCount> findByProduct(Product product);

	Page<InventoryCount> findByProduct(Product product, Pageable pageable);

	@Query("SELECT sum(quantity) FROM InventoryCount WHERE product.id = ?1")
	Double findProductQuantityCounted(int productId);

	@Query("SELECT sum(quantity) FROM InventoryCount WHERE product.id = ?1 AND dateCounted < ?2")
	Double findProductQuantityCountedUntil(int productId, Date until);
}
