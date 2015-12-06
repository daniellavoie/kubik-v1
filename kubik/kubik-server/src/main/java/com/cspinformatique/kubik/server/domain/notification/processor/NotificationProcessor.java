package com.cspinformatique.kubik.server.domain.notification.processor;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.kos.model.KubikNotification.Type;

public interface NotificationProcessor {
	void process(KubikNotification kubikNotification);
	
	Type getType();
}
