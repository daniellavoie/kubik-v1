package com.cspinformatique.kubik.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;

public interface ProductRepository extends
		PagingAndSortingRepository<Product, Integer> {
	@Query("SELECT id FROM Product WHERE dilicomReference = ?")
	List<Integer> findIdByDilicomReference(boolean dilicomReference);
	
	public Product findByEan13AndSupplier(String ean13, Supplier supplier);

	@Query("SELECT product FROM Product product WHERE ean13 LIKE :query OR extendedLabel LIKE :query OR publisher LIKE :query OR collection LIKE :query OR author LIKE :query OR isbn LIKE :query")
	public Page<Product> search(@Param("query") String query, Pageable pageable);
}
