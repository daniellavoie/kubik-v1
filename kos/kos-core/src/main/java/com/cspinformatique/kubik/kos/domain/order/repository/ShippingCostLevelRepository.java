package com.cspinformatique.kubik.kos.domain.order.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.kos.model.order.ShippingCostLevel;

public interface ShippingCostLevelRepository extends JpaRepository<ShippingCostLevel, Integer> {
	List<ShippingCostLevel> findByWeightGreaterThan(Integer weight, Pageable pageable);
}
