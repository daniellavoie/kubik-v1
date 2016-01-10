package com.cspinformatique.kubik.kos.domain.kubik.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.kos.domain.kubik.service.KubikNotificationService;
import com.cspinformatique.kubik.kos.model.kubik.KubikNotification;

@Component
public class KosNotificationTask {
	@Autowired
	private KubikNotificationService kubikNotificationService;

	@Scheduled(fixedDelay = 1000 * 1)
	public void processPendingNotifications() {
		for (KubikNotification kubikNotification : kubikNotificationService
				.findByStatus(KubikNotification.Status.TO_PROCESS, new Sort(Direction.ASC, "id"))) {
			kubikNotificationService.process(kubikNotification);
		}
	}
}
