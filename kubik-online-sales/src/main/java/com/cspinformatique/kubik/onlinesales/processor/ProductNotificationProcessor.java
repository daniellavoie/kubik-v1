package com.cspinformatique.kubik.onlinesales.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.model.kos.KosNotification.Type;
import com.cspinformatique.kubik.onlinesales.service.ProductService;

@Component
public class ProductNotificationProcessor implements NotificationProcessor {
	@Resource
	private ProductService productService;
	
	@Override
	public Type getType() {
		return Type.PRODUCT;
	}

	@Override
	public void process(KosNotification kosNotification) {
		productService.processKosNotification(kosNotification);
	}

}
