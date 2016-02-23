package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.server.model.sales.ShippingCostLevel;

public interface ShippingCostLevelService {
	ShippingCostLevel calculateShippingCostLevel(int weight);
}
