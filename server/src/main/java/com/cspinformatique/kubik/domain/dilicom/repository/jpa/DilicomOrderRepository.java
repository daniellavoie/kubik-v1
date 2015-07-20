package com.cspinformatique.kubik.domain.dilicom.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.dilicom.DilicomOrder;
import com.cspinformatique.kubik.model.dilicom.DilicomOrder.Status;

public interface DilicomOrderRepository extends
		JpaRepository<DilicomOrder, Integer> {
	
	List<DilicomOrder> findByStatus(Status status);
}
