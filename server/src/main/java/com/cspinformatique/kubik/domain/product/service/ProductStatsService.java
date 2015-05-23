package com.cspinformatique.kubik.domain.product.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.ProductStats;

public interface ProductStatsService {
	Page<ProductStats> findProductStats(Date startDate, Date endDate,
			Pageable pageable);
}
