package com.cspinformatique.kubik.server.domain.dilicom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.dilicom.model.ReferenceNotification;
import com.cspinformatique.kubik.server.domain.dilicom.model.ReferenceNotification.Status;
import com.cspinformatique.kubik.server.domain.dilicom.service.ReferenceNotificationService;
import com.cspinformatique.kubik.server.model.product.Product;

@Controller
@RequestMapping("/notification")
public class ReferenceNotificationController {
	@Autowired
	private ReferenceNotificationService referenceNotificationService;
	
	@RequestMapping(value = "/{status}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Long countByStatus(@PathVariable Status status) {
		return this.referenceNotificationService.countByStatus(status);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<ReferenceNotification> findAll(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "date") String sortBy) {
		return this.referenceNotificationService
				.findByStatus(Status.NEW, new PageRequest(page, resultPerPage,
						direction != null ? direction : Direction.DESC, sortBy));
	}

	@RequestMapping
	public String getNotificationsPage() {
		return "dilicom/notifications";
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{notificationId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void validateNotification(@PathVariable Integer notificationId,
			@RequestBody Product product) {
		this.referenceNotificationService.validate(notificationId, product);
	}
}
