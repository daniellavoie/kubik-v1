package com.cspinformatique.kubik.server.domain.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.Supplier;

public interface ProductRepository extends
		PagingAndSortingRepository<Product, Integer> {
	int countByCategory(Category category);
	
	int countByImagesValidated(boolean imagesValidated);
	
	@Query("SELECT id FROM Product")
	List<Integer> findAllIds();
	
	List<Product> findByCategory(Category category);
	
	@Query("SELECT id FROM Product WHERE dilicomReference = ?1")
	List<Integer> findIdByDilicomReference(boolean dilicomReference);

	Product findByEan13(String ean13);

	Product findByEan13AndSupplier(String ean13, Supplier supplier);

	Iterable<Product> findBySupplier(Supplier supplier);
	
	@Query("SELECT product FROM Product product WHERE LENGTH(ean13) < 12")
	List<Product> findInvalidEan13();

	@Query("SELECT product FROM Product product WHERE category = ?1 ORDER BY RAND()")
	Page<Product> findRandomByCategory(Category category, Pageable pageable);

	@Query("SELECT product FROM Product product WHERE imagesValidated = ?1 ORDER BY RAND()")
	Page<Product> findRandomByImagesValidated(boolean imagesValidated, Pageable pageable);

	@Query("SELECT product FROM Product product WHERE category is null ORDER BY RAND()")
	Page<Product> findRandomWithoutCategory(Pageable pageable);
	
	@Query("SELECT product FROM Product product WHERE ean13 LIKE ?1 OR extendedLabel LIKE ?1 OR publisher LIKE ?1 OR collection LIKE ?1 OR author LIKE ?1 OR isbn LIKE ?1")
	Page<Product> search(String query, Pageable pageable);
}
