package com.cspinformatique.kubik.kos.domain.notification.processor;

import com.cspinformatique.kubik.server.model.kos.KosNotification;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Type;

public interface NotificationProcessor {
	void process(KosNotification kosNotification);
	
	Type getType();
}
