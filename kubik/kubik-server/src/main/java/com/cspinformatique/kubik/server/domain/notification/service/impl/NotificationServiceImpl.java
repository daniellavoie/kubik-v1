package com.cspinformatique.kubik.server.domain.notification.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.server.domain.notification.processor.NotificationProcessors;
import com.cspinformatique.kubik.server.domain.notification.service.NotificationService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Resource
	private ProductService productService;

	@Resource
	private NotificationProcessors processors;

	@Override
	public void processNotification(KubikNotification kubikNotification) {
		processors.getProcessor(kubikNotification.getType()).process(kubikNotification);
	}
}
