package com.cspinformatique.kubik.domain.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder;

@Controller
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@RequestMapping("/confirmOrders")
	public void confirmAllOrders() {
		this.purchaseOrderService.confirmAllOrders();
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<PurchaseOrder> findAll(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "date") String sortBy) {
		return this.purchaseOrderService.findAll(new PageRequest(page,
				resultPerPage, direction != null ? direction : Direction.DESC,
				sortBy));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PurchaseOrder findOne(@PathVariable int id) {
		return this.purchaseOrderService.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getOrderDetailsPage(@PathVariable int id, Model model) {
		model.addAttribute("id", id);

		return "purchase/order/order-details";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getOrdersPage() {
		return "purchase/order/orders";
	}

	@RequestMapping("/fixSubmitedDate")
	public String fixSubmitedDate() {
		this.purchaseOrderService.fixSubmitedDate();
		
		return "purchase/order/orders";
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PurchaseOrder save(
			@RequestBody PurchaseOrder purchaseOrder) {
		return this.purchaseOrderService.save(purchaseOrder);
	}
}
