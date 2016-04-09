package com.cspinformatique.kubik.server.domain.warehouse.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.warehouse.repository.InventoryCountRepository;
import com.cspinformatique.kubik.server.domain.warehouse.service.InventoryCountService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.warehouse.InventoryCount;

@Service
public class InventoryCountServiceImpl implements InventoryCountService {
	@Resource
	InventoryCountRepository inventoryCountRepository;

	@Resource
	ProductInventoryService productInventoryService;

	@Resource
	StocktakingService stocktakingService;

	@Override
	public List<InventoryCount> findByDateCountedAfter(Date dateCounted) {
		return inventoryCountRepository.findByDateCountedAfter(dateCounted);
	}

	@Override
	public List<InventoryCount> findByProduct(Product product) {
		return inventoryCountRepository.findByProduct(product);
	}

	@Override
	public Page<InventoryCount> findByProduct(Product product, Pageable pageable) {
		return inventoryCountRepository.findByProduct(product, pageable);
	}

	@Override
	public double findProductQuantityCounted(int productId) {
		Double result = inventoryCountRepository.findProductQuantityCounted(productId);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public double findProductQuantityCountedUntil(int productId, Date until) {
		Double result = inventoryCountRepository.findProductQuantityCountedUntil(productId, until);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public InventoryCount save(InventoryCount inventoryCount) {
		inventoryCount = inventoryCountRepository.save(inventoryCount);

		productInventoryService.updateInventory(inventoryCount.getProduct());
		stocktakingService.applyInventoryAdjustments(inventoryCount.getProduct(), inventoryCount.getQuantity());

		return inventoryCount;
	}
}
