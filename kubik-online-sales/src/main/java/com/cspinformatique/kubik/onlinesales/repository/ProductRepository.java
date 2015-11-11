package com.cspinformatique.kubik.onlinesales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.onlinesales.model.Category;
import com.cspinformatique.kubik.onlinesales.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByCategory(Category category);

	Product findByKubikId(int kubikId);
}
