package com.cspinformatique.kubik.server.domain.kos.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.domain.kos.service.KosNotificationService;
import com.cspinformatique.kubik.server.model.kos.KosNotification;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Status;

@Component
public class KosNotificationTask {
	@Autowired
	private KosNotificationService kosNotificationService;

	@Value("${kos.notification.enabled}")
	private boolean notificationEnabled;

	@Scheduled(fixedDelay = 1000 * 1)
	public void processPendingNotifications() {
		if (notificationEnabled)
			for (KosNotification kosNotification : kosNotificationService.findByStatus(Status.TO_PROCESS,
					new Sort(Direction.ASC, "id")))
				kosNotificationService.process(kosNotification);

	}
}
