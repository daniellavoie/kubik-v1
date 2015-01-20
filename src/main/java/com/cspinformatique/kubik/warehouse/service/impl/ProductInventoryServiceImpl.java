package com.cspinformatique.kubik.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.warehouse.model.ProductInventory;
import com.cspinformatique.kubik.warehouse.repository.ProductInventoryRepository;
import com.cspinformatique.kubik.warehouse.service.ProductInventoryService;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
	@Autowired private ProductInventoryRepository productInventoyRepository;
	
	@Override
	public ProductInventory addInventory(Product product, double quantity){
		ProductInventory productInventory = this.findByProduct(product);

		productInventory.setQuantityOnHand(productInventory.getQuantityOnHand() + quantity);

		return this.save(productInventory);
	}
	
	@Override
	public ProductInventory findByProduct(Product product) {
		ProductInventory productInventory = this.productInventoyRepository.findByProduct(product);

		if(productInventory == null){
			productInventory = new ProductInventory(null, product, 0d, 0d);
		}
		
		return productInventory;
	}

	@Override
	public ProductInventory removeInventory(Product product, double quantity){
		ProductInventory productInventory = this.findByProduct(product);

		productInventory.setQuantityOnHand(productInventory.getQuantityOnHand() - quantity);

		return this.save(productInventory);
	}
	
	@Override
	public ProductInventory save(ProductInventory productInventory) {
		return this.productInventoyRepository.save(productInventory);
	}

}
