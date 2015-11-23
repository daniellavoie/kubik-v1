package com.cspinformatique.kubik.domain.warehouse.service;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.warehouse.model.InventoryExtract;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.warehouse.ProductInventory;

public interface ProductInventoryService {
	void deleteByProduct(Product product);

	Page<ProductInventory> findAll(Pageable pageable);

	ProductInventory findByProduct(Product product);

	List<Integer> findProductIdWithInventory();

	InventoryExtract generateInventoryExtraction(String separator, DecimalFormat decimalFormat);

	void updateInventory(Product product);

	void updateInventory(ProductInventory productInventory);

	void updateInventories();

	ProductInventory save(ProductInventory productInventory);
}
