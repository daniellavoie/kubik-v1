package com.cspinformatique.kubik.purchase.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.service.PurchaseOrderService;

@Controller
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {
	@Autowired private PurchaseOrderService purchaseOrderService;
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<PurchaseOrder> findAll(){
		return this.purchaseOrderService.findAll();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PurchaseOrder findOne(@PathVariable int id){
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

	
	@RequestMapping(method={RequestMethod.POST, RequestMethod.PUT}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PurchaseOrder save(@RequestBody PurchaseOrder purchaseOrder){
		return this.purchaseOrderService.save(purchaseOrder);
	}
}
