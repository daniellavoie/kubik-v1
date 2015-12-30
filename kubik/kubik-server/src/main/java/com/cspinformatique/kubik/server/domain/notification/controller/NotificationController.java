package com.cspinformatique.kubik.server.domain.notification.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.server.domain.notification.service.NotificationService;

@Controller
@RequestMapping("/kubikNotification")
public class NotificationController {
	@Resource
	private NotificationService notificationService;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody void save(@RequestBody KubikNotification notification) {
		notificationService.processNotification(notification);
	}
}
