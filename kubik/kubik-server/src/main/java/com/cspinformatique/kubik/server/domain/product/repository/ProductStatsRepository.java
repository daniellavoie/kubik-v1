package com.cspinformatique.kubik.server.domain.product.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.product.ProductStats;

public interface ProductStatsRepository {
	Page<ProductStats> findAll(Date startDate, Date endDate, boolean withoutInventory, Pageable pageable);
	
	ProductStats findByProductId(int productId, Date startDate, Date endDate);
}
