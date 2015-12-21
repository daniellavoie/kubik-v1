package com.cspinformatique.kubik.server.domain.product.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.Supplier;

public interface ProductService {
	Product buildProductFromReference(Reference reference);

	int countByCategory(Category category);
	
	int countByImagesValidated(boolean imagesValidated);
	
	Iterable<Product> findByEan13(String ean13);
	
	Product findByEan13AndSupplier(String ean13, Supplier supplier);

	List<Product> findByCategory(Category category);
	
	Iterable<Product> findBySupplier(Supplier supplier);
	
	Page<Product> findAll(Pageable pageable);
	
	Product findOne(int id);
	
	Product findRandomByCategory(Category category);
	
	Product findRandomByImagesValidated(boolean imagesValidated);
	
	Set<String> getProductIdsCache();
	
	void mergeProduct(Product sourceProduct, Product targetProduct);
	
	Product save(Product product);
	
	Product save(Product product, boolean skipBroadleafNotification);
	
	Page<Product> search(String query, Pageable pageable);
}
