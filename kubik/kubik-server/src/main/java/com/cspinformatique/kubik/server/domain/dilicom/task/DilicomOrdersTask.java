package com.cspinformatique.kubik.server.domain.dilicom.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.domain.dilicom.service.DilicomOrderService;

@Component
@ConditionalOnProperty(name = "kubik.dilicom.enabled")
public class DilicomOrdersTask {
	@Autowired
	private DilicomOrderService dilicomOrderService;

	@Scheduled(fixedDelay = 1000 * 60 * 1)
	public void sendPendingDilicomOrders() {
		this.dilicomOrderService.sendPendingDilicomOrders();
	}
}
