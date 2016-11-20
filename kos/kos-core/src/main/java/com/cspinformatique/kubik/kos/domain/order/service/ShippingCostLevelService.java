package com.cspinformatique.kubik.kos.domain.order.service;

import com.cspinformatique.kubik.kos.model.order.ShippingCostLevel;

public interface ShippingCostLevelService {
	ShippingCostLevel calculateShippingCostLevel(int weight);
}
