package com.cspinformatique.kubik.domain.warehouse.service;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.warehouse.ProductInventory;

public interface ProductInventoryService {
	ProductInventory findByProduct(Product product);
	
	void updateInventory(Product product);
	
	ProductInventory save(ProductInventory productInventory);
}
