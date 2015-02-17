package com.cspinformatique.kubik.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.purchase.model.Rma;
import com.cspinformatique.kubik.purchase.service.RmaService;

@Controller
@RequestMapping("/rma")
public class RmaController {
	@Autowired
	private RmaService rmaService;

	public Page<Rma> findAll(@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "creationDate") String sortBy) {
		return this.rmaService.findAll(new PageRequest(page, resultPerPage,
				direction != null ? direction : Direction.DESC, sortBy));
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Rma save(@RequestBody Rma rma) {
		return this.rmaService.save(rma);
	}
}
