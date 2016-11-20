package com.cspinformatique.kubik.kos.domain.notification.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.kos.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.kos.KosNotification;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Type;

@Component
public class ProductInventoryNotificationProcessor implements NotificationProcessor {
	@Resource
	private ProductService productService;

	@Override
	public Type getType() {
		return Type.PRODUCT_INVENTORY;
	}


	@Override
	public void process(KosNotification kosNotification) {
		productService.processProductNotification(kosNotification);
	}
}
