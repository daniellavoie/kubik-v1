package com.cspinformatique.kubik.domain.product.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.product.ProductStats;

public interface ProductStatsRepository {
	Page<ProductStats> findProductStats(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, Pageable pageable);
}
