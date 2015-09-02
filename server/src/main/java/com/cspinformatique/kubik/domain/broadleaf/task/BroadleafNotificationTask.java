package com.cspinformatique.kubik.domain.broadleaf.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.domain.broadleaf.service.BroadleafNotificationService;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification.Status;

@Component
public class BroadleafNotificationTask {
	@Autowired
	private BroadleafNotificationService broadleafNotificationService;

	@Value("${broadleaf.activated}")
	private boolean activated;

	@Scheduled(fixedDelay = 1000 * 1)
	public void processPendingNotifications() {
		if (activated) {
			for (BroadleafNotification broadleafNotification : broadleafNotificationService
					.findByStatus(Status.TO_PROCESS)) {
				broadleafNotificationService.process(broadleafNotification);
			}
		}
	}
}
