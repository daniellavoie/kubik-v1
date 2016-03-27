package com.cspinformatique.kubik.server.domain.warehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingCategory;
import com.cspinformatique.kubik.server.model.warehouse.StocktakingProduct;

public interface StocktakingProductRepository extends JpaRepository<StocktakingProduct, Long> {
	@Query("SELECT count(1) FROM StocktakingProduct WHERE product.id = ?1 AND category.id = ?2")
	int countCategoriesWithProduct(int productId, long categoryId);

	Optional<StocktakingProduct> findByProductAndCategory(Product product, StocktakingCategory category);
}
