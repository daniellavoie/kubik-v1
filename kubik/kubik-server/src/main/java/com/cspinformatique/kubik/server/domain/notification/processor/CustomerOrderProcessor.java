package com.cspinformatique.kubik.server.domain.notification.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.kos.model.KubikNotification.Type;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerOrderService;

@Component
public class CustomerOrderProcessor implements NotificationProcessor {
	@Resource
	private CustomerOrderService customerOrderService;

	@Override
	public void process(KubikNotification kubikNotification) {
		customerOrderService.processNotification(kubikNotification);
	}

	@Override
	public Type getType() {
		return Type.CUSTOMER_ORDER;
	}

}
