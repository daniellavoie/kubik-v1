package com.cspinformatique.kubik.domain.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.sales.model.InvoiceStatus;

@Controller
@RequestMapping("/invoiceStatus")
public class InvoiceStatusController {
	@Autowired private InvoiceStatusService invoiceStatusService;
	
	@RequestMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody InvoiceStatus findOne(@PathVariable String id){
		return this.invoiceStatusService.findOne(id);
	}
}
