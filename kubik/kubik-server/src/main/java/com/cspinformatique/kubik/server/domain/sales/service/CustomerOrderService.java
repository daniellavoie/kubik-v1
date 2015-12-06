package com.cspinformatique.kubik.server.domain.sales.service;

import com.cspinformatique.kubik.kos.model.KubikNotification;

public interface CustomerOrderService {
	void processNotification(KubikNotification kubikNotification);
}
