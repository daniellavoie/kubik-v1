package com.cspinformatique.kubik.domain.warehouse.service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.warehouse.model.ProductInventory;

public interface ProductInventoryService {
	ProductInventory findByProduct(Product product);
	
	void updateInventory(Product product);
	
	ProductInventory save(ProductInventory productInventory);
}
