package com.cspinformatique.kubik.server.domain.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.sales.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

}
