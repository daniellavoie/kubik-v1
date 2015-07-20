package com.cspinformatique.kubik.domain.dilicom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.dilicom.service.DilicomOrderService;
import com.cspinformatique.kubik.model.dilicom.DilicomOrder;

@Controller
@RequestMapping("/dilicomOrder")
public class DilicomOrderController {
	@Autowired private DilicomOrderService dilicomOrderService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<DilicomOrder> findAll(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "creationDate") String sortBy){
		return this.dilicomOrderService.findAll(new PageRequest(page, resultPerPage,
				direction != null ? direction : Direction.DESC, sortBy));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getDilicomOrdersPage(){
		return "dilicom/dilicom-orders";
	}
}
