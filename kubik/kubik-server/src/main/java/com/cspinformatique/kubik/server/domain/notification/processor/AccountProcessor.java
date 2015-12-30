package com.cspinformatique.kubik.server.domain.notification.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.kos.model.KubikNotification.Type;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerService;

@Component
public class AccountProcessor implements NotificationProcessor {
	@Resource
	private CustomerService customerService;

	@Override
	public void process(KubikNotification kubikNotification) {
		customerService.processNotification(kubikNotification);
	}

	@Override
	public Type getType() {
		return Type.ACCOUNT;
	}

}
