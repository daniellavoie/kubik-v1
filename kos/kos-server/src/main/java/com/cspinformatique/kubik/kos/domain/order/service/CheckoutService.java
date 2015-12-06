package com.cspinformatique.kubik.kos.domain.order.service;

import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

public interface CheckoutService {
	void checkout(CustomerOrder customerOrder, String nonce);
	
	String generateClientToken();
}
