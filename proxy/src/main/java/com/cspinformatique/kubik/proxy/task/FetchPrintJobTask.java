package com.cspinformatique.kubik.proxy.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.proxy.service.PrintService;

@Component
public class FetchPrintJobTask {
	@Autowired private PrintService printService;
	
	@Scheduled(fixedDelay=2000)
	public void fetchPrintJobs(){
		this.printService.executePrintJobs();
	}
}