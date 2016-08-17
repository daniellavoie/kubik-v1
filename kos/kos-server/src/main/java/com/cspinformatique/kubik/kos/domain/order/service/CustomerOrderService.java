package com.cspinformatique.kubik.kos.domain.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.Status;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;
import com.querydsl.core.types.Predicate;

public interface CustomerOrderService {
	
	CustomerOrder addDetail(CustomerOrder customerOrder, CustomerOrderDetail customerOrderDetail);
	
	CustomerOrder loadOpenCustomerOrder(Account account, String uuid);
	
	Page<CustomerOrder> findAll(Predicate predicate, Pageable pageable);
	
	Page<CustomerOrder> findByAccountAndStatusIn(Account account, List<Status> status, Pageable pageable);
	
	CustomerOrder findOne(long id);
	
	boolean isActivated();
	
	CustomerOrder save(CustomerOrder customerOrder);
}
