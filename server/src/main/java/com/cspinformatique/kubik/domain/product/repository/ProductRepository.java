package com.cspinformatique.kubik.domain.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.Supplier;

public interface ProductRepository extends
		PagingAndSortingRepository<Product, Integer> {
	@Query("SELECT id FROM Product WHERE dilicomReference = ?")
	List<Integer> findIdByDilicomReference(boolean dilicomReference);

	Iterable<Product> findByEan13(String ean13);

	Product findByEan13AndSupplier(String ean13, Supplier supplier);

	Iterable<Product> findBySupplier(Supplier supplier);

	@Query("SELECT product FROM Product product WHERE ean13 LIKE :query OR extendedLabel LIKE :query OR publisher LIKE :query OR collection LIKE :query OR author LIKE :query OR isbn LIKE :query")
	Page<Product> search(@Param("query") String query, Pageable pageable);
}
