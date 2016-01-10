package com.cspinformatique.kubik.server.domain.sales.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.braintreegateway.Transaction;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Resource
	PaymentService paymentService;

	@ResponseBody
	@RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Transaction loadTransaction(@PathVariable String transactionId) {
		return paymentService.loadTransaction(transactionId);
	}
}
