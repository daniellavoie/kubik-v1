package com.cspinformatique.kubik.server.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductDoubles;
import com.cspinformatique.kubik.server.model.product.Supplier;

public interface ProductService {
	Product buildProductFromReference(Reference reference);

	int countByCategory(Category category);
	
	int countByImagesValidated(boolean imagesValidated);
	
	List<Integer> findAllIds();
	
	List<ProductDoubles> findAllProductDoubles();
	
	Product findByEan13(String ean13);
	
	List<Product> findByCategory(Category category);
	
	Product findByEan13AndSupplier(String ean13, Supplier supplier);

	List<Product> findByEan13Doubles(String ean13);
	
	Iterable<Product> findBySupplier(Supplier supplier);
	
	Page<Product> findAll(Pageable pageable);
	
	Product findOne(int id);
	
	Product findRandomByCategory(Category category);
	
	Product findRandomByImagesValidated(boolean imagesValidated);
	
	void mergeProduct(Product sourceProduct, Product targetProduct);
	
	Product save(Product product);
	
	Product save(Product product, boolean skipBroadleafNotification);
	
	Page<Product> search(String query, Pageable pageable);
}
