package com.cspinformatique.kubik.domain.product.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.repository.ProductStatsRepository;
import com.cspinformatique.kubik.domain.product.service.ProductStatsService;
import com.cspinformatique.kubik.model.product.ProductStats;

@Service
public class ProductStatsServiceImpl implements ProductStatsService {
	@Autowired
	private ProductStatsRepository productStatsRepository;

	@Override
	public Page<ProductStats> findAll(Date startDate, Date endDate,
			Pageable pageable) {
		return productStatsRepository.findAll(startDate, endDate,
				pageable);
	}
	
	@Override
	public ProductStats findByProductId(int productId, Date startDate, Date endDate){
		return this.productStatsRepository.findByProductId(productId, startDate, endDate);
	}

}
