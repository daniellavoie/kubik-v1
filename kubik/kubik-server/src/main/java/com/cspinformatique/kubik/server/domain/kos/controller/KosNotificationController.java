package com.cspinformatique.kubik.server.domain.kos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.server.domain.kos.service.KosNotificationService;
import com.cspinformatique.kubik.server.model.kos.KosNotification;

@Controller
@RequestMapping("/kos")
public class KosNotificationController {
	@Autowired
	private KosNotificationService kosNotificationService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<KosNotification> findAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(defaultValue = "DESC") Direction direction,
			@RequestParam(defaultValue = "creationDate") String sortBy) {
		return kosNotificationService.findAll(new PageRequest(page, resultPerPage, direction, sortBy));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody KosNotification findOne(@PathVariable int id) {
		return kosNotificationService.findOne(id);
	}

	@RequestMapping(method = RequestMethod.GET, params = "initialLoad", produces = MediaType.APPLICATION_JSON_VALUE)
	public void initialLoad() {
		kosNotificationService.initialLoad();
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody KosNotification save(@RequestBody KosNotification kosNotification) {
		return kosNotificationService.save(kosNotification);
	}
}
