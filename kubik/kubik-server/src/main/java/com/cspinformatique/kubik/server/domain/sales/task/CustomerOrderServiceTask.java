package com.cspinformatique.kubik.server.domain.sales.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.domain.sales.service.CustomerOrderService;

@Component
public class CustomerOrderServiceTask {
	private CustomerOrderService customerOrderService;

	public CustomerOrderServiceTask(CustomerOrderService customerOrderService) {
		this.customerOrderService = customerOrderService;
	}

	@Scheduled(fixedDelay = 15 * 1000)
	public void processConfirmations() {
		customerOrderService.processConfirmations();
	}

	@Scheduled(fixedDelay = 15 * 1000 * 60)
	public void recover() {
		customerOrderService.recoverConfirmations();
	}
}
