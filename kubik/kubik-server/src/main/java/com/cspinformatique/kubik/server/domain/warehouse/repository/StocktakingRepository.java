package com.cspinformatique.kubik.server.domain.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.warehouse.Stocktaking;

public interface StocktakingRepository extends JpaRepository<Stocktaking, Long> {

}
