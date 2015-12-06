package com.cspinformatique.kubik.server.domain.notification.service;

import com.cspinformatique.kubik.kos.model.KubikNotification;

public interface NotificationService {
	void processNotification(KubikNotification kubikNotification);
}
