package com.cspinformatique.kubik.onlinesales.processor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.model.kos.KosNotification.Type;
import com.cspinformatique.kubik.onlinesales.service.CategoryService;

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
