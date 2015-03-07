package com.cspinformatique.kubik.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.sales.model.CustomerCredit;
import com.cspinformatique.kubik.sales.service.CustomerCreditService;

@Controller
@RequestMapping("/customerCredit")
public class CustomerCreditController {
	@Autowired private CustomerCreditService customerCreditService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<CustomerCredit> find(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "date") String sortBy) {
		PageRequest pageRequest = new PageRequest(page, resultPerPage,
				direction != null ? direction : Direction.DESC, sortBy);

		return this.customerCreditService.findAll(pageRequest);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerCredit findOne(@PathVariable int id){
		return this.customerCreditService.findOne(id);
	}
	
	@RequestMapping(value = "/{id}/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer findNextCustomerCredit(@PathVariable int id){
		return this.customerCreditService.findNext(id);
	}
	
	@RequestMapping(value = "/{id}/previous", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer findPreviousCustomerCredit(@PathVariable int id){
		return this.customerCreditService.findPrevious(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerCreditDetailsPage() {
		return "sales/customer-credit/credit-details";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerCreditsPage() {
		return "sales/customer-credit/credits-page";
	}
	
	@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerCredit save(@RequestBody CustomerCredit customerCredit){
		return this.customerCreditService.save(customerCredit);
	}
}
