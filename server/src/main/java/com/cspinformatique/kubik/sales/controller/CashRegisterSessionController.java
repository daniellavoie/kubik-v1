package com.cspinformatique.kubik.sales.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.sales.model.Invoice;
import com.cspinformatique.kubik.sales.service.CashRegisterService;
import com.cspinformatique.kubik.sales.service.CashRegisterSessionService;
import com.cspinformatique.kubik.sales.service.InvoiceService;

@Controller
@RequestMapping("/cashRegister")
public class CashRegisterSessionController {
	@Autowired
	private CashRegisterService cashRegisterService;
	@Autowired
	private CashRegisterSessionService cashRegisterSessionService;

	@Autowired
	private InvoiceService invoiceService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCashRegisterPage() {
		return "sales/cash-register/cash-register";
	}

	@RequestMapping(value = "/session/invoice", method = RequestMethod.GET)
	private @ResponseBody Iterable<Invoice> findInvoices(
			HttpServletRequest request) {
		return invoiceService
				.findInvoiceByCashRegisterSessionAndInDraft(this.cashRegisterSessionService
						.findByCashRegister(this.cashRegisterService
								.getCashRegister(request)));
	}

	@RequestMapping(value = "/session/invoice", params= "new", method = RequestMethod.GET)
	private @ResponseBody Invoice generateNewInvoice(
			HttpServletRequest request) {
		return invoiceService.generateNewInvoice(this.cashRegisterSessionService
				.findByCashRegister(this.cashRegisterService
						.getCashRegister(request)));
	}
}
