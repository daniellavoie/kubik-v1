package com.cspinformatique.kubik.domain.broadleaf.controller;

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

import com.cspinformatique.kubik.domain.broadleaf.service.BroadleafNotificationService;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification;

@Controller
@RequestMapping("/broadleafNotification")
public class BroadleafNotificationController {
	@Autowired
	private BroadleafNotificationService broadleafNotificationService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<BroadleafNotification> findAll(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(defaultValue = "DESC") Direction direction,
			@RequestParam(defaultValue = "creationDate") String sortBy) {
		return broadleafNotificationService.findAll(new PageRequest(page,
				resultPerPage, direction, sortBy));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BroadleafNotification findOne(@PathVariable int id) {
		return broadleafNotificationService.findOne(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody BroadleafNotification save(
			@RequestBody BroadleafNotification broadleafNotification) {
		return broadleafNotificationService.save(broadleafNotification);
	}
}
