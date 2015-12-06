package com.cspinformatique.kubik.kos.domain.notification.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.notification.processor.NotificationProcessors;
import com.cspinformatique.kubik.kos.domain.notification.service.NotificationService;
import com.cspinformatique.kubik.kos.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.kos.KosNotification;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Resource
	private ProductService productService;

	@Resource
	private NotificationProcessors processors;

	@Override
	public void processNotification(KosNotification kosNotification) {
		processors.getProcessor(kosNotification.getType()).process(kosNotification);
	}
}
