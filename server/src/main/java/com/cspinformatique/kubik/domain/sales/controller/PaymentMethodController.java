package com.cspinformatique.kubik.domain.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.sales.service.PaymentMethodService;
import com.cspinformatique.kubik.sales.model.PaymentMethod;

@Controller
@RequestMapping("/paymentMethod")
public class PaymentMethodController {
	@Autowired private PaymentMethodService paymentMethodService;
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<PaymentMethod> findAll(){
		return this.paymentMethodService.findAll();
	}
	
	@RequestMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PaymentMethod findOne(@PathVariable String id){
		return this.paymentMethodService.findOne(id);
	}
}
