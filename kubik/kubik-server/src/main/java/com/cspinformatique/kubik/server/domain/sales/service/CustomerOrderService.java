package com.cspinformatique.kubik.server.domain.sales.service;

import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;

import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

public interface CustomerOrderService {

	Page<CustomerOrder> findAll(MultiValueMap<String, String> parameters);

	CustomerOrder findOne(Long id);

	CustomerOrder save(CustomerOrder customerOrder);
	
	void processConfirmations();
	
	void recoverConfirmations();
}
