package com.cspinformatique.kubik.domain.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.model.product.Category;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.SubCategory;
import com.cspinformatique.kubik.model.product.Supplier;

public interface ProductRepository extends
		PagingAndSortingRepository<Product, Integer> {
	@Query("SELECT count(1) FROM Product WHERE subCategory.category = ?1")
	int countByCategory(Category category);
	
	int countBySubCategory(SubCategory subCategory);
	
	@Query("FROM Product WHERE subCategory.category = ?1")
	List<Product> findByCategory(Category category);
	
	List<Product> findBySubCategory(SubCategory subCategory);
	
	@Query("SELECT id FROM Product WHERE dilicomReference = ?1")
	List<Integer> findIdByDilicomReference(boolean dilicomReference);

	Iterable<Product> findByEan13(String ean13);

	Product findByEan13AndSupplier(String ean13, Supplier supplier);

	Iterable<Product> findBySupplier(Supplier supplier);

	@Query("SELECT product FROM Product product WHERE subCategory is null ORDER BY RAND()")
	Page<Product> findRandomWithoutSubCategory(Pageable pageable);
	
	@Query("SELECT product FROM Product product WHERE ean13 LIKE ?1 OR extendedLabel LIKE ?1 OR publisher LIKE ?1 OR collection LIKE ?1 OR author LIKE ?1 OR isbn LIKE ?1")
	Page<Product> search(String query, Pageable pageable);
}
