package com.cspinformatique.kubik.server.domain.warehouse.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.InventoryCount;

public interface InventoryCountService {
	List<InventoryCount> findByDateCountedAfter(Date dateCounted);
	
	List<InventoryCount> findByProduct(Product product);

	Page<InventoryCount> findByProduct(Product product, Pageable pageable);
	
	double findProductQuantityCounted(int productId);
	
	double findProductQuantityCountedUntil(int productId, Date until);
	
	InventoryCount save(InventoryCount inventoryCount);
}