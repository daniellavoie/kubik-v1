package com.cspinformatique.kubik.server.domain.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.purchase.DiscountType;

public interface DiscountTypeRepository extends JpaRepository<DiscountType, String> {

}
