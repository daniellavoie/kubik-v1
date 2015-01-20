package com.cspinformatique.kubik.warehouse.service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.warehouse.model.ProductInventory;

public interface ProductInventoryService {
	public ProductInventory addInventory(Product product, double quantity);
	
	public ProductInventory findByProduct(Product product);
	
	public ProductInventory removeInventory(Product product, double quantity);
	
	public ProductInventory save(ProductInventory productInventory);
}
