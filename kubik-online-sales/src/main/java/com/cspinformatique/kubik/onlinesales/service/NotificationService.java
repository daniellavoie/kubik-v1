package com.cspinformatique.kubik.onlinesales.service;

import com.cspinformatique.kubik.model.kos.KosNotification;

public interface NotificationService {
	void processNotification(KosNotification kosNotification);
}
