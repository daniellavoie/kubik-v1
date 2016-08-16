package com.cspinformatique.kubik.server.domain.titelive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cspinformatique.kubik.server.domain.titelive.service.TiteLiveService;

@RestController
@RequestMapping("/tite-live")
public class TiteLiveController {
	private TiteLiveService titeLiveService;

	@Autowired
	public TiteLiveController(TiteLiveService titeLiveService) {
		this.titeLiveService = titeLiveService;
	}
	
	@RequestMapping
	public void sendInventory(){
		titeLiveService.sendInventory();
	}
}
