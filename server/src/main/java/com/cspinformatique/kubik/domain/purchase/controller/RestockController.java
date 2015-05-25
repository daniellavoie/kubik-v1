package com.cspinformatique.kubik.domain.purchase.controller;

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

import com.cspinformatique.kubik.domain.purchase.service.RestockService;
import com.cspinformatique.kubik.model.purchase.Restock;
import com.cspinformatique.kubik.model.purchase.Restock.Status;

@Controller
@RequestMapping("/restock")
public class RestockController {
	@Autowired
	private RestockService restockService;

	@RequestMapping(value = "/{status}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Long countByStatus(@PathVariable Status status) {
		return this.restockService.countByStatus(status);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Restock> findAll(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "date") String sortBy) {
		return this.restockService.findByStatus(Status.OPEN, new PageRequest(
				page, resultPerPage, direction != null ? direction
						: Direction.DESC, sortBy));
	}

	@RequestMapping(value = "/{restockId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Restock findOne(@PathVariable int restockId) {
		return this.restockService.findOne(restockId);
	}

	@RequestMapping
	public String getNotificationsPage() {
		return "purchase/restock/restocks";
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Restock save(@RequestBody Restock restock) {
		return this.restockService.save(restock);
	}
}
