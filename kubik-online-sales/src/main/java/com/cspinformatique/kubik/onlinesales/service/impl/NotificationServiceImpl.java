package com.cspinformatique.kubik.onlinesales.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.processor.NotificationProcessors;
import com.cspinformatique.kubik.onlinesales.service.NotificationService;
import com.cspinformatique.kubik.onlinesales.service.ProductService;

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
