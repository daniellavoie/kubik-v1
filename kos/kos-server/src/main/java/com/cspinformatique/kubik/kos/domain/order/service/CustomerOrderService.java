package com.cspinformatique.kubik.kos.domain.order.service;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;

public interface CustomerOrderService {
	
	CustomerOrder addDetail(CustomerOrder customerOrder, CustomerOrderDetail customerOrderDetail);
	
	CustomerOrder loadOpenCustomerOrder(Account account, String uuid);
	
	CustomerOrder findOne(long id);
	
	CustomerOrder save(CustomerOrder customerOrder);
}
