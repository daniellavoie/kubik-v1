package com.cspinformatique.kubik.kos.domain.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.order.repository.ShippingCostLevelRepository;
import com.cspinformatique.kubik.kos.domain.order.service.ShippingCostLevelService;
import com.cspinformatique.kubik.kos.model.order.ShippingCostLevel;

@Service
public class ShippingCostLevelServiceImpl implements ShippingCostLevelService {
	@Resource
	private ShippingCostLevelRepository shippingCostLevelRepository;

	@Override
	public ShippingCostLevel calculateShippingCostLevel(int weight) {
		List<ShippingCostLevel> shippingCostLevels = shippingCostLevelRepository.findByWeightGreaterThan(weight,
				new PageRequest(0, 1, Direction.ASC, "weight"));

		if (shippingCostLevels.isEmpty()) {
			throw new RuntimeException("Shipping cost level not for the order weighting " + weight + " grams).");
		}

		return shippingCostLevels.get(0);
	}

}
