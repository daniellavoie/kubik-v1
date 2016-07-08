package com.cspinformatique.kubik.server.domain.titelive.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.domain.titelive.service.TiteLiveService;

@Component
@ConditionalOnProperty(name = "titelivre.enabled")
public class TiteLivreExtractionTask {
	@Autowired
	private TiteLiveService titeLiveService;

	@Scheduled(cron = "0 0 20 * * ?")
	public void executeExtraction() {
		titeLiveService.sendInventoryToTiteLiveServer();
	}
}
