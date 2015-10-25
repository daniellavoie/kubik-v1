package com.cspinformatique.kubik.domain.warehouse.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.warehouse.service.InventoryCountService;
import com.cspinformatique.kubik.model.warehouse.InventoryCount;

@Controller
@RequestMapping("/inventoryCount")
public class InventoryCountController {
	@Resource
	private InventoryCountService inventoryCountService;

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody InventoryCount save(@RequestBody InventoryCount inventoryCount) {
		return inventoryCountService.save(inventoryCount);
	}
}
