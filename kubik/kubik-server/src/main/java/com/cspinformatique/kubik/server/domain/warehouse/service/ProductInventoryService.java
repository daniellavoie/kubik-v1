package com.cspinformatique.kubik.server.domain.warehouse.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.domain.warehouse.model.InventoryExtract;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.ProductInventory;

public interface ProductInventoryService {
	double calculateProductOnHandUntil(int productId, Date until);
	
	void deleteByProduct(Product product);

	List<ProductInventory> findAll();

	Page<ProductInventory> findAll(Pageable pageable);

	ProductInventory findByProduct(Product product);

	List<Integer> findProductIdWithInventory();

	InventoryExtract generateInventoryExtraction(String separator, DecimalFormat decimalFormat);
	
	InventoryExtract generateInventoryExtraction(String separator, DecimalFormat decimalFormat, Date until);

	void updateInventory(Product product);

	void updateInventory(ProductInventory productInventory);

	void updateInventories();

	ProductInventory save(ProductInventory productInventory);
}
