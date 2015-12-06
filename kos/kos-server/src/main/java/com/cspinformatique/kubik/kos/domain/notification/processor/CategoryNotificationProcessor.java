package com.cspinformatique.kubik.kos.domain.notification.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.kos.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.model.kos.KosNotification;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Type;

@Component
public class CategoryNotificationProcessor implements NotificationProcessor {
	@Resource
	CategoryService categoryService;
	
	@Override
	public Type getType() {
		return Type.CATEGORY;
	}

	@Override
	public void process(KosNotification kosNotification) {
		categoryService.processKosNotification(kosNotification);
	}

}
