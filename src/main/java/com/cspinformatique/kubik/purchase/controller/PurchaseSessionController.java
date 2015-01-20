package com.cspinformatique.kubik.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.service.PurchaseSessionService;

@Controller
@RequestMapping("/purchaseSession")
public class PurchaseSessionController {
	@Autowired
	private PurchaseSessionService purchaseSessionService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<PurchaseSession> findAll() {
		return this.purchaseSessionService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PurchaseSession findOne(@PathVariable int id) {
		return this.purchaseSessionService.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getSessionDetailsPage(@PathVariable int id, Model model) {
		model.addAttribute("id", id);

		return "purchase/session/session-details";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getSessionsPage() {
		return "purchase/session/sessions";
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PurchaseSession save(@RequestBody PurchaseSession purchaseSession) {
		return this.purchaseSessionService.save(purchaseSession);
	}
}
