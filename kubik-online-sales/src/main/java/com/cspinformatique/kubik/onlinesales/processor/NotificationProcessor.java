package com.cspinformatique.kubik.onlinesales.processor;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.model.kos.KosNotification.Type;

public interface NotificationProcessor {
	void process(KosNotification kosNotification);
	
	Type getType();
}
