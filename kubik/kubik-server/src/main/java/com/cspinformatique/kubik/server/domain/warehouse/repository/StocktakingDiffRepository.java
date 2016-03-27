package com.cspinformatique.kubik.server.domain.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.warehouse.StocktakingDiff;

public interface StocktakingDiffRepository extends JpaRepository<StocktakingDiff, Long> {
	
}
