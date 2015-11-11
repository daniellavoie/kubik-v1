package com.cspinformatique.kubik.onlinesales.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.service.NotificationService;

@Controller
@RequestMapping("/kosNotification")
public class NotificationController {
	@Resource
	private NotificationService notificationService;

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public @ResponseBody void save(@RequestBody KosNotification notification) {
		notificationService.processNotification(notification);
	}
}
