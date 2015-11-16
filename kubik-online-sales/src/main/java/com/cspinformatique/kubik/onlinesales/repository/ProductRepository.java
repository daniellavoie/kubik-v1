package com.cspinformatique.kubik.onlinesales.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.onlinesales.model.Category;
import com.cspinformatique.kubik.onlinesales.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByCategory(Category category);

	Product findByKubikId(int kubikId);

	@Query("SELECT product FROM Product product WHERE "
			+ "(?1 is null OR title like ?1 ) AND "
			+ "(?2 is null OR author like ?2) AND "
			+ "(?3 is null OR datePublished >= ?3) AND "
			+ "(?4 is null OR datePublished <= ?4) AND "
			+ "(?5 is null OR manufacturer like ?5) AND "
			+ "(?6 is null OR price >= ?6) AND "
			+ "(?7 is null OR price <= ?7) AND "
			+ "(?8 is null OR title like ?8 OR author like ?8 OR manufacturer like ?8)")
	Page<Product> search(String title, String author, Date publishFrom, Date publishUntil, String manufacturer,
			Double priceFrom, Double priceTo, String query, Pageable pageable);

	@Query("SELECT product FROM Product product WHERE "
			+ "(?1 is null OR title like ?1 ) AND "
			+ "(?2 is null OR author like ?2) AND "
			+ "category in (?3) AND "
			+ "(?4 is null OR datePublished >= ?4) AND "
			+ "(?5 is null OR datePublished <= ?5) AND "
			+ "(?6 is null OR manufacturer like ?6) AND "
			+ "(?7 is null OR price >= ?7) AND "
			+ "(?8 is null OR price <= ?8) AND "
			+ "(?9 is null OR title like ?9 OR author like ?9 OR manufacturer like ?9)")
	Page<Product> search(String title, String author, List<Category> categories, Date publishFrom, Date publishUntil, String manufacturer,
			Double priceFrom, Double priceTo, String query, Pageable pageable);
}
