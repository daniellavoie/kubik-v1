package com.cspinformatique.kubik.domain.warehouse.service;

import java.util.List;

import com.cspinformatique.kubik.domain.warehouse.model.InventoryExtractLine;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.warehouse.ProductInventory;

public interface ProductInventoryService {
	void deleteByProduct(Product product);
	
	ProductInventory findByProduct(Product product);
	
	List<Integer> findProductIdWithInventory();
	
	List<InventoryExtractLine> generateInventoryExtraction();
	
	void updateInventory(Product product);
	
	ProductInventory save(ProductInventory productInventory);
}
