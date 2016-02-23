package com.cspinformatique.kubik.server.domain.sales.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.sales.ShippingCostLevel;

public interface ShippingCostLevelRepository extends JpaRepository<ShippingCostLevel, Integer> {
	List<ShippingCostLevel> findByWeightGreaterThan(Integer weight, Pageable pageable);
}
