package com.cspinformatique.kubik.kos.domain.notification.service;

import com.cspinformatique.kubik.server.model.kos.KosNotification;

public interface NotificationService {
	void processNotification(KosNotification kosNotification);
}
